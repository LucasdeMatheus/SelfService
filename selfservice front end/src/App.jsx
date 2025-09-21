import { useState, useEffect, useRef } from 'react';
import './App.css';

function App() {
  const [weight, setWeight] = useState(null);
  const [calculated, setCalculated] = useState(null);
  const [created, setCreated] = useState(null);
  const [qrCodeImg, setQrCodeImg] = useState(null);
  const [showPaymentOverlay, setShowPaymentOverlay] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState(null);
  const [statusMessage, setStatusMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [paid, setPaid] = useState(false);
  const [countdown, setCountdown] = useState(5);

  const wsRef = useRef(null);
  const PRICE_PER_KG = 10.0; // exemplo: R$10 por kg

  useEffect(() => {
    const connect = () => {
      if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) return;

      wsRef.current = new WebSocket('ws://localhost:8080/ws/weight');

      wsRef.current.onopen = () => console.log('Conectado ao WebSocket');
      wsRef.current.onclose = () => {
        console.log('WebSocket desconectado, reconectando em 3s...');
        setTimeout(connect, 3000);
      };
      wsRef.current.onerror = (err) => console.error('Erro no WebSocket', err);

      wsRef.current.onmessage = (event) => {
        try {
          console.log('Mensagem recebida do WebSocket:', event.data);

          const data = JSON.parse(event.data); // parse JSON
          const pesoRecebido = parseFloat(data.weight); // pega o peso
          const valor = parseFloat(data.value);        // pega o valor

          if (!isNaN(pesoRecebido) && !isNaN(valor)) {
            setWeight(pesoRecebido);
            setCalculated({ weight: pesoRecebido, value: valor });
          }
        } catch (err) {
          console.error('Erro ao processar peso:', err);
        }
      };

    };

    connect();

    return () => {
      if (wsRef.current) wsRef.current.close();
    };
  }, []);

  const handleShowPayment = () => {
    setShowPaymentOverlay(true);
    setStatusMessage('Escolha uma forma de pagamento');
  };

  const handleCancel = () => {
    setShowPaymentOverlay(false);
    setPaymentMethod(null);
    setQrCodeImg(null);
    setStatusMessage('');
    setLoading(false);
    setPaid(false);
    setCountdown(5);
  };

  const handlePaymentSelect = async (method) => {
    setPaymentMethod(method);
    setShowPaymentOverlay(false);
    setStatusMessage('Processando pagamento...');
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ weight })
      });
      const data = await response.json();
      setCreated(data);

      if (method === 'PIX' && calculated) {
        const qrBody = {
          type: 'pix',
          JsonNode: {
            chave: 'xxx',
            cidade: 'SAO PAULO',
            nome: 'Lucas Matheus',
            valor: calculated.value,
            txid: 'TX202509201200'
          },
          content: 'https://github.com/LucasdeMatheus/qr-code',
          background: "#ffffff",
          qrCodeColor: "#000000",
          expirationDate: '2025-12-31T23:59:59'
        };

        const qrResponse = await fetch('https://qr-code-a1zp.onrender.com/generate-qr', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(qrBody)
        });

        const blob = await qrResponse.blob();
        const url = URL.createObjectURL(blob);
        setQrCodeImg(url);
        setStatusMessage('Aguardando pagamento PIX...');
        setLoading(false);

        setTimeout(() => startCountdown(), 5000);
      } else {
        setQrCodeImg(null);
        setLoading(false);
        setStatusMessage(`Pagamento via ${method} selecionado.\nFaça o pagamento na maquininha.`);
        setTimeout(() => startCountdown(), 5000);
      }
    } catch (error) {
      console.error('Erro ao criar entry ou gerar QR Code:', error);
      setLoading(false);
      setStatusMessage('Erro ao processar pagamento. Tente novamente.');
    }
  };

  const startCountdown = () => {
    setPaid(true);
    setCountdown(5);
    setStatusMessage('Pagamento recebido! \n Obrigado pela preferência!\n:)');
    const interval = setInterval(() => {
      setCountdown(prev => {
        if (prev === 1) {
          clearInterval(interval);
          handleCancel();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
  };

  return (
    <div className="app-container">
      <h1 className="app-title">Self Service</h1>

      <div className="peso-container">
        <span className="peso-label">PESO:</span>
        <span className="peso-value">{weight !== null ? weight.toFixed(2) : '--'} kg</span>
      </div>

      {calculated && (
        <div className="valor-container">
          <span className="valor-label">VALOR:</span>
          <span className="valor-value">R$ {calculated.value.toFixed(2)}</span>
        </div>
      )}

      {calculated && (
        <div className="button-container">
          <button className="btn-create" onClick={handleShowPayment}>
            IR PARA PAGAMENTO
          </button>
        </div>
      )}

      {showPaymentOverlay && (
        <div className="overlay">
          <div className="container">
            {calculated && (
              <div className="valor-container">
                <span className="valor-label">VALOR:</span>
                <span className="valor-value">R$ {calculated.value.toFixed(2)}</span>
              </div>
            )}
            <h2>Escolha a forma de pagamento</h2>
            <div className="payment-buttons">
              <button onClick={() => handlePaymentSelect('PIX')}>PIX</button>
              <button onClick={() => handlePaymentSelect('Crédito')}>Crédito</button>
              <button onClick={() => handlePaymentSelect('Débito')}>Débito</button>
            </div>
            <button className="btn-cancel" onClick={handleCancel}>Cancelar</button>
          </div>
        </div>
      )}

      {paymentMethod && !paid && (
        <div className="overlay">
          <div className="container">
            {loading && <p className="status-message">Carregando...</p>}
            {!loading && statusMessage && <p className="status-message">{statusMessage}</p>}
            {paymentMethod === 'PIX' && qrCodeImg && !loading && (
              <img src={qrCodeImg} alt="QR Code" />
            )}
            <button className="btn-cancel" onClick={handleCancel}>Cancelar</button>
          </div>
        </div>
      )}

      {paid && (
        <div className="overlay">
          <div className="container">
            <h2>{statusMessage}</h2>
            <p className="status-message">Fechando em {countdown} segundos...</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
