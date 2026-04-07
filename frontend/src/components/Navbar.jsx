import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">📚 Student Feedback</Link>
      </div>
      
      <div className="navbar-links">
        <Link to="/courses">Courses</Link>
        
        {isAuthenticated ? (
          <>
            <Link to="/my-feedback">My Feedback</Link>
            {(user.role === 'ADMIN' || user.role === 'INSTRUCTOR') && (
              <Link to="/manage-courses">Manage Courses</Link>
            )}
            <div className="user-menu">
              <span className="user-name">{user.username}</span>
              <span className="user-role">{user.role}</span>
              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </div>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register" className="register-link">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
