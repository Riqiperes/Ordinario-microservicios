import React, { useEffect, useState } from 'react';
import { getOrdenes, getProductos, getClientes, crearOrden } from '../services/api';

const OrderManager = () => {
  const [ordenes, setOrdenes] = useState([]);
  const [productos, setProductos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [nuevaOrden, setNuevaOrden] = useState({ clienteId: '', productoId: '', cantidad: 1 });
  const [mensaje, setMensaje] = useState({ texto: '', tipo: '' });

  const fetchData = async () => {
    try {
      const [ordRes, prodRes, cliRes] = await Promise.all([
        getOrdenes(),
        getProductos(),
        getClientes()
      ]);
      setOrdenes(ordRes.data);
      setProductos(prodRes.data);
      setClientes(cliRes.data);
    } catch (err) {
      console.error('Error fetching data', err);
    }
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (nuevaOrden.cantidad <= 0) {
      setMensaje({ texto: 'La cantidad debe ser mayor a cero.', tipo: 'danger' });
      return;
    }

    try {
      await crearOrden(nuevaOrden);
      setMensaje({ texto: 'Orden creada exitosamente.', tipo: 'success' });
      setNuevaOrden({ clienteId: '', productoId: '', cantidad: 1 });
      fetchData();
      setTimeout(() => setMensaje({ texto: '', tipo: '' }), 3000);
    } catch (err) {
      const errorMsg = err.response?.data?.message || 'Error al crear la orden.';
      setMensaje({ texto: errorMsg, tipo: 'danger' });
    }
  };

  return (
    <div className="container mt-4">
      <h2>Gestión de Órdenes</h2>

      {/* Formulario de Creación (Arriba) */}
      <div className="card shadow-sm mb-5">
        <div className="card-header bg-success text-white">
          <h5 className="mb-0">Nueva Orden</h5>
        </div>
        <div className="card-body">
          {mensaje.texto && <div className={`alert alert-${mensaje.tipo}`}>{mensaje.texto}</div>}
          <form onSubmit={handleSubmit} className="row g-3">
            <div className="col-md-4">
              <label className="form-label">Cliente</label>
              <select 
                className="form-select" 
                value={nuevaOrden.clienteId}
                onChange={(e) => setNuevaOrden({...nuevaOrden, clienteId: e.target.value})}
                required
              >
                <option value="">Seleccione un cliente</option>
                {clientes.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
              </select>
            </div>
            <div className="col-md-4">
              <label className="form-label">Producto</label>
              <select 
                className="form-select" 
                value={nuevaOrden.productoId}
                onChange={(e) => setNuevaOrden({...nuevaOrden, productoId: e.target.value})}
                required
              >
                <option value="">Seleccione un producto</option>
                {productos.map(p => <option key={p.id} value={p.id}>{p.nombre} (Stock: {p.stock})</option>)}
              </select>
            </div>
            <div className="col-md-2">
              <label className="form-label">Cantidad</label>
              <input 
                type="number" 
                className="form-control" 
                value={nuevaOrden.cantidad}
                onChange={(e) => setNuevaOrden({...nuevaOrden, cantidad: parseInt(e.target.value)})}
                required
              />
            </div>
            <div className="col-md-2 d-flex align-items-end">
              <button type="submit" className="btn btn-success w-100">Crear Orden</button>
            </div>
          </form>
        </div>
      </div>

      {/* Tabla de Visualización (Abajo) */}
      <div className="card shadow-sm">
        <div className="card-header bg-dark text-white">
          <h5 className="mb-0">Listado de Órdenes</h5>
        </div>
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead>
              <tr>
                <th>ID Orden</th>
                <th>Cliente</th>
                <th>Producto</th>
                <th>Cant.</th>
                <th>Total</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {ordenes.map(o => (
                <tr key={o.id}>
                  <td><small className="text-muted">{o.id}</small></td>
                  <td>{clientes.find(c => c.id === o.clienteId)?.nombre || o.clienteId}</td>
                  <td>{productos.find(p => p.id === o.productoId)?.nombre || o.productoId}</td>
                  <td>{o.cantidad}</td>
                  <td><strong>${o.montoTotal?.toFixed(2)}</strong></td>
                  <td>
                    <span className={`badge rounded-pill bg-${o.estado === 'PAGADA' ? 'success' : 'warning text-dark'}`}>
                      {o.estado}
                    </span>
                  </td>
                </tr>
              ))}
              {ordenes.length === 0 && (
                <tr>
                  <td colSpan="6" className="text-center py-4 text-muted">No hay órdenes registradas.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default OrderManager;
