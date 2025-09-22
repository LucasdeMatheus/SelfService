export const printReceipt = (weight, value, orderId) => {
  const receiptHtml = `
    <html>
      <head>
        <style>
          html, body {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            font-size: 12px;
            display: flex;
            justify-content: center; /* centraliza verticalmente */
            align-items: center;     /* centraliza horizontalmente */
          }
          .container {
            width: 60mm; 
            text-align: center; /* centraliza o texto */
          }
          h2 {
            margin: 0 0 5px 0;
            font-size: 14px;
          }
          p {
            margin: 2px 0;
            font-size: 12px;
          }
          .line {
            border-top: 1px dashed #000;
            margin: 3px 0;
            width: 100%;
          }
        </style>
      </head>
      <body>
        <div class="container">
          <h2>Self Service</h2>
          <p>Peso: ${weight.toFixed(2)} kg</p>
          <p>Valor: R$ ${value.toFixed(2)}</p>
          <p>ID Pedido: ${orderId}</p>
          <div class="line"></div>
          <p>Obrigado pela preferência!</p>
        </div>
      </body>
    </html>
  `;

  const printWindow = window.open('', '', 'width=240,height=150');
  printWindow.document.write(receiptHtml);
  printWindow.document.close();
  printWindow.focus();
  printWindow.print();
  printWindow.close();
};
