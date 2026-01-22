import { Link } from 'react-router-dom';
import type { Movie } from '../types';

interface Props {
  movie: Movie;
}

export default function MovieCard({ movie }: Props) {
  return (
    <Link to={`/movies/${movie.id}`} className="card movie-card">
      <img
        src={movie.posterUrl || '/placeholder.jpg'}
        alt={movie.title}
        onError={(e) => {
          (e.target as HTMLImageElement).src = 'https://via.placeholder.com/200x300?text=No+Image';
        }}
      />
      <div className="movie-card-info">
        <h3 className="movie-card-title">{movie.title}</h3>
        <p className="movie-card-meta">
          {movie.rating} | {movie.durationMinutes}분
        </p>
      </div>
    </Link>
  );
}
