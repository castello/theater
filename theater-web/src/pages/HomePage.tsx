import { useEffect, useState } from 'react';
import { movieApi } from '../api/client';
import MovieCard from '../components/MovieCard';
import Loading from '../components/Loading';
import type { Movie } from '../types';

export default function HomePage() {
  const [showingMovies, setShowingMovies] = useState<Movie[]>([]);
  const [upcomingMovies, setUpcomingMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const [showing, upcoming] = await Promise.all([
          movieApi.getShowing(),
          movieApi.getUpcoming(),
        ]);
        setShowingMovies(showing);
        setUpcomingMovies(upcoming);
      } catch (error) {
        console.error('Failed to fetch movies:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMovies();
  }, []);

  if (loading) return <Loading />;

  return (
    <div style={{ paddingTop: '80px' }}>
      <section className="section">
        <div className="container">
          <h2 className="section-title">현재 상영작</h2>
          {showingMovies.length > 0 ? (
            <div className="movie-grid">
              {showingMovies.map((movie) => (
                <MovieCard key={movie.id} movie={movie} />
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>상영 중인 영화가 없습니다.</p>
          )}
        </div>
      </section>

      <section className="section">
        <div className="container">
          <h2 className="section-title">개봉 예정</h2>
          {upcomingMovies.length > 0 ? (
            <div className="movie-grid">
              {upcomingMovies.map((movie) => (
                <MovieCard key={movie.id} movie={movie} />
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>개봉 예정 영화가 없습니다.</p>
          )}
        </div>
      </section>
    </div>
  );
}
