import React, { useEffect, useState } from 'react';
import { getEnvios } from '../services/api';

const ShipmentList = () => {
  const [envios, setEnvios] = useState([]);

  const fetchEnvios = async () => {
    try {
      const response = await getEnvios();
      setEnvios(response.data);
    } catch (err) {
      console.error('Error fetching shipments', err);
    }
  };

  useEffect(() => {
    fetchEnvios();
    const interval = setInterval(fetchEnvios, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="container mt-4">
      <h2>Seguimiento de Envíos</h2>
      <table className="table table-striped">
        <thead className="table-primary">
          <tr>
            <th>ID Envío</th>
            <th>ID Orden</th>
            <th>Dirección</th>
            <th>Estado</th>
            <th>Fecha</th>
          </tr>
        </thead>
        <tbody>
          {envios.map(e => (
            <tr key={e.id}>
              <td>{e.id}</td>
              <td>{e.ordenId}</td>
              <td>{e.direccion}</td>
              <td>
                <span className="badge bg-info text-dark">{e.estadoEnvio}</span>
              </td>
              <td>{new Date(e.fechaEnvio).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ShipmentList;
