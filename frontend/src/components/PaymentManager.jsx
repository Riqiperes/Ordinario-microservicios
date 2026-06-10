import React, { useEffect, useState } from 'react';
import { getOrdenes, crearPago, getPagos } from '../services/api';

const PaymentManager = () => {
  const [ordenes, setOrdenes] = useState([]);
  const [pagos, setPagos] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [pago, setPago] = useState({ ordenId: '', monto: 0 });
  const [mensaje, setMensaje] = useState({ texto: '', tipo: '' });

  const fetchData = async () => {
    try {
      const [ordRes, pagRes] = await Promise.all([getOrdenes(), getPagos()]);
      // Solo órdenes pendientes para el selector de arriba
      setOrdenes(ordRes.data.filter(o => o.estado !== 'PAGADA'));
      setPagos(pagRes.data);
    } catch (err) {
      console.error('Error fetching data', err);
    }
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 5000);
    return () => clearInterval(interval);
  }, []);

  const handlePago = async (e) => {
    e.preventDefault();
    try {
      await crearPago(pago);
      setMensaje({ texto: '¡Pago registrado exitosamente!', tipo: 'success' });
      setPago({ ordenId: '', monto: 0 });
      setSearchTerm('');
      fetchData();
      setTimeout(() => setMensaje({ texto: '', tipo: '' }), 3000);
    } catch (err) {
      setMensaje({ texto: 'Error al procesar el pago.', tipo: 'danger' });
    }
  };

  const filteredOrders = ordenes.filter(o => 
    o.id.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const selectOrder = (o) => {
    setPago({ ...pago, ordenId: o.id, monto: o.montoTotal });
    setSearchTerm(o.id);
  };

  return (
    <div className="container mt-4">
      <h2>Gestión de Pagos</h2>

      {/* Registro de Pago (Arriba) */}
      <div className="card shadow-sm mb-5">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">Registrar Pago</h5>
        </div>
        <div className="card-body">
          {mensaje.texto && <div className={`alert alert-${mensaje.tipo}`}>{mensaje.texto}</div>}
          <form onSubmit={handlePago} className="row g-3">
            <div className="col-md-6">
              <label className="form-label">ID de Orden (Buscador/Manual)</label>
              <div className="input-group">
                <input 
                  type="text" 
                  className="form-control" 
                  placeholder="ID de Orden..."
                  value={searchTerm}
                  onChange={(e) => {
                    setSearchTerm(e.target.value);
                    setPago({...pago, ordenId: e.target.value});
                  }}
                  required
                />
                <button className="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown"></button>
                <ul className="dropdown-menu dropdown-menu-end w-100 shadow" style={{maxHeight: '200px', overflowY: 'auto'}}>
                  {filteredOrders.map(o => (
                    <li key={o.id}>
                      <button className="dropdown-item" type="button" onClick={() => selectOrder(o)}>
                        {o.id.substring(0,10)}... (${o.montoTotal})
                      </button>
                    </li>
                  ))}
                  {filteredOrders.length === 0 && <li><span className="dropdown-item-text">Sin resultados</span></li>}
                </ul>
              </div>
            </div>
            <div className="col-md-4">
              <label className="form-label">Monto</label>
              <input 
                type="number" 
                step="0.01" 
                className="form-control" 
                value={pago.monto}
                onChange={(e) => setPago({...pago, monto: parseFloat(e.target.value)})}
                required 
              />
            </div>
            <div className="col-md-2 d-flex align-items-end">
              <button type="submit" className="btn btn-primary w-100">Pagar</button>
            </div>
          </form>
        </div>
      </div>

      {/* Historial de Pagos (Abajo) */}
      <div className="card shadow-sm">
        <div className="card-header bg-dark text-white">
          <h5 className="mb-0">Historial de Pagos Realizados</h5>
        </div>
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead>
              <tr>
                <th>ID Pago</th>
                <th>ID Orden</th>
                <th>Monto</th>
                <th>Estado</th>
                <th>Fecha</th>
              </tr>
            </thead>
            <tbody>
              {pagos.map(p => (
                <tr key={p.id}>
                  <td><small className="text-muted">{p.id}</small></td>
                  <td>{p.ordenId}</td>
                  <td><span className="text-success fw-bold">${p.monto.toFixed(2)}</span></td>
                  <td><span className="badge bg-success">{p.estado}</span></td>
                  <td>{new Date(p.fechaPago).toLocaleString()}</td>
                </tr>
              ))}
              {pagos.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-4 text-muted">No hay pagos registrados.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default PaymentManager;
