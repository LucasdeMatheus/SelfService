import { useState, useEffect, useRef } from 'react';

function WebSocketComponent({ onMessage }) {
  const wsRef = useRef(null);
  const [connected, setConnected] = useState(false);

  const connect = () => {
    wsRef.current = new WebSocket('ws://localhost:8080/ws/weight');

    wsRef.current.onopen = () => {
      console.log('WebSocket conectado');
      setConnected(true);
    };

    wsRef.current.onclose = () => {
      console.log('WebSocket desconectado');
      setConnected(false);
    };

    wsRef.current.onmessage = (event) => {
      console.log('Mensagem recebida:', event.data);
      onMessage?.(event.data);
    };
  };

  useEffect(() => {
    connect();

    return () => {
      wsRef.current?.close();
    };
  }, []);

  return !connected ? (
  <div className='overlay'>
    <div className='container'>
      <p className='status-message'>
        ❌<br />
        Conexão perdida
      </p>
    </div>
  </div>
) : null;


}

export default WebSocketComponent;
