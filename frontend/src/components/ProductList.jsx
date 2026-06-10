import React, { useEffect, useState } from 'react';
import { getProductos, eliminarProducto, crearProducto } from '../services/api';

const ProductList = () => {
  const [productos, setProductos] = useState([]);
  const [nuevoProducto, setNuevoProducto] = useState({ nombre: '', stock: 0, precio: 0 });
  const [error, setError] = useState('');

  const fetchProductos = async () => {
    try {
      const response = await getProductos();
      setProductos(response.data);
    } catch (err) {
      console.error('Error fetching products', err);
    }
  };

  useEffect(() => {
    fetchProductos();
  }, []);

  const handleEliminar = async (id) => {
    try {
      await eliminarProducto(id);
      fetchProductos();
    } catch (err) {
      if (err.response && err.response.status === 400) {
        alert(err.response.data.message || 'No se puede eliminar el producto porque está asociado a una orden.');
      } else {
        alert('Error al eliminar el producto');
      }
    }
  };

  const handleCrear = async (e) => {
    e.preventDefault();
    if (nuevoProducto.stock <= 0) {
      setError('El stock debe ser mayor a cero.');
      return;
    }
    try {
      await crearProducto(nuevoProducto);
      setNuevoProducto({ nombre: '', stock: 0, precio: 0 });
      setError('');
      fetchProductos();
    } catch (err) {
      alert('Error al crear el producto');
    }
  };

  return (
    <div className="container mt-4">
      <h2>Productos</h2>
      
      <form onSubmit={handleCrear} className="mb-4 p-3 border rounded">
        <h4>Crear Producto</h4>
        {error && <div className="alert alert-danger">{error}</div>}
        <div className="row">
          <div className="col">
            <input 
              type="text" 
              placeholder="Nombre" 
              className="form-control"
              value={nuevoProducto.nombre}
              onChange={(e) => setNuevoProducto({...nuevoProducto, nombre: e.target.value})}
              required
            />
          </div>
          <div className="col">
            <input 
              type="number" 
              placeholder="Stock" 
              className="form-control"
              value={nuevoProducto.stock}
              onChange={(e) => setNuevoProducto({...nuevoProducto, stock: parseInt(e.target.value)})}
              required
            />
          </div>
          <div className="col">
            <input 
              type="number" 
              step="0.01"
              placeholder="Precio" 
              className="form-control"
              value={nuevoProducto.precio}
              onChange={(e) => setNuevoProducto({...nuevoProducto, precio: parseFloat(e.target.value)})}
              required
            />
          </div>
          <div className="col">
            <button type="submit" className="btn btn-primary">Agregar</button>
          </div>
        </div>
      </form>

      <table className="table table-striped">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Stock</th>
            <th>Precio</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {productos.map(p => (
            <tr key={p.id}>
              <td>{p.nombre}</td>
              <td>{p.stock}</td>
              <td>${p.precio}</td>
              <td>
                <button 
                  className="btn btn-danger btn-sm" 
                  onClick={() => handleEliminar(p.id)}
                >
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;
