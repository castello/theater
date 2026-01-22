import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import MoviesPage from './pages/MoviesPage';
import MovieDetailPage from './pages/MovieDetailPage';
import TheatersPage from './pages/TheatersPage';
import BookingPage from './pages/BookingPage';
import MyReservationsPage from './pages/MyReservationsPage';

function App() {
  return (
    <BrowserRouter>
      <Header />
      <main>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/movies" element={<MoviesPage />} />
          <Route path="/movies/:id" element={<MovieDetailPage />} />
          <Route path="/theaters" element={<TheatersPage />} />
          <Route path="/booking/:showtimeId" element={<BookingPage />} />
          <Route path="/my-reservations" element={<MyReservationsPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
