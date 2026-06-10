import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard = () => {
  return (
    <div className="container mt-5">
      <div className="jumbotron p-5 mb-4 bg-light border rounded-3 text-center">
        <h1 className="display-4">Sistema de Microservicios</h1>
        <p className="lead">Gestión integral de clientes, productos, órdenes y pagos.</p>
        <hr className="my-4" />
        <div className="row g-4 mt-2">
          <div className="col-md-4">
            <div className="card h-100 shadow-sm">
              <div className="card-body">
                <h5>Inventario</h5>
                <p>Gestiona productos, stock y precios.</p>
                <Link to="/productos" className="btn btn-outline-primary">Ver Productos</Link>
              </div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card h-100 shadow-sm">
              <div className="card-body">
                <h5>Ventas</h5>
                <p>Crea órdenes y gestiona clientes.</p>
                <Link to="/ordenes" className="btn btn-outline-success mb-2 w-100">Nueva Orden</Link>
                <Link to="/clientes" className="btn btn-outline-info w-100">Clientes</Link>
              </div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card h-100 shadow-sm">
              <div className="card-body">
                <h5>Logística</h5>
                <p>Seguimiento de pagos y envíos.</p>
                <Link to="/listado-ordenes" className="btn btn-outline-warning mb-2 w-100">Pagos</Link>
                <Link to="/envios" className="btn btn-outline-dark w-100">Envíos</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
