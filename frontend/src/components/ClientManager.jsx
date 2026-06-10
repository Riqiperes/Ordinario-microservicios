import React, { useEffect, useState } from 'react';
import { getClientes, crearCliente } from '../services/api';

const ClientManager = () => {
  const [clientes, setClientes] = useState([]);
  const [nuevoCliente, setNuevoCliente] = useState({ nombre: '', email: '', telefono: '' });

  const fetchClientes = async () => {
    try {
      const response = await getClientes();
      setClientes(response.data);
    } catch (err) {
      console.error('Error fetching clients', err);
    }
  };

  useEffect(() => {
    fetchClientes();
  }, []);

  const handleCrear = async (e) => {
    e.preventDefault();
    try {
      await crearCliente(nuevoCliente);
      setNuevoCliente({ nombre: '', email: '', telefono: '' });
      fetchClientes();
    } catch (err) {
      alert('Error al crear el cliente');
    }
  };

  return (
    <div className="container mt-4">
      <h2>Clientes</h2>
      
      <form onSubmit={handleCrear} className="mb-4 p-3 border rounded">
        <h4>Registrar Cliente</h4>
        <div className="row">
          <div className="col">
            <input 
              type="text" 
              placeholder="Nombre" 
              className="form-control"
              value={nuevoCliente.nombre}
              onChange={(e) => setNuevoCliente({...nuevoCliente, nombre: e.target.value})}
              required
            />
          </div>
          <div className="col">
            <input 
              type="email" 
              placeholder="Email" 
              className="form-control"
              value={nuevoCliente.email}
              onChange={(e) => setNuevoCliente({...nuevoCliente, email: e.target.value})}
              required
            />
          </div>
          <div className="col">
            <input 
              type="text" 
              placeholder="Teléfono" 
              className="form-control"
              value={nuevoCliente.telefono}
              onChange={(e) => setNuevoCliente({...nuevoCliente, telefono: e.target.value})}
            />
          </div>
          <div className="col">
            <button type="submit" className="btn btn-primary">Registrar</button>
          </div>
        </div>
      </form>

      <table className="table table-striped">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Teléfono</th>
          </tr>
        </thead>
        <tbody>
          {clientes.map(c => (
            <tr key={c.id}>
              <td>{c.nombre}</td>
              <td>{c.email}</td>
              <td>{c.telefono}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ClientManager;
