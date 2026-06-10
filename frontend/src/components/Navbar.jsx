import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow mb-4">
      <div className="container">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav mx-auto">
            <li className="nav-item"><Link className="nav-link px-3 fw-bold text-info" to="/">Inicio</Link></li>
            <li className="nav-item"><Link className="nav-link px-3" to="/clientes">Clientes</Link></li>
            <li className="nav-item"><Link className="nav-link px-3" to="/productos">Productos</Link></li>
            <li className="nav-item"><Link className="nav-link px-3" to="/ordenes">Ordenes</Link></li>
            <li className="nav-item"><Link className="nav-link px-3" to="/pagos">Pagos</Link></li>
            <li className="nav-item"><Link className="nav-link px-3" to="/envios">Envios</Link></li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
