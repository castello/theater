import { Link, useLocation } from 'react-router-dom';

export default function Header() {
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <header className="header">
      <div className="container header-content">
        <Link to="/" className="logo">
          THEATER
        </Link>
        <nav className="nav">
          <Link to="/" className={isActive('/') ? 'active' : ''}>
            홈
          </Link>
          <Link to="/movies" className={isActive('/movies') ? 'active' : ''}>
            영화
          </Link>
          <Link to="/theaters" className={isActive('/theaters') ? 'active' : ''}>
            극장
          </Link>
          <Link to="/my-reservations" className={isActive('/my-reservations') ? 'active' : ''}>
            예매내역
          </Link>
        </nav>
      </div>
    </header>
  );
}
