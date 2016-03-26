package com.michael.moviecake;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieFragment extends Fragment {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    MovieAdapter mMovieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FetchMovieTask().execute();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        mMovieAdapter = new MovieAdapter(getActivity());
        Log.v(LOG_TAG, (Integer.toString(gridView.getColumnWidth())));
        gridView.setAdapter(mMovieAdapter);

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            TmdbApi tmdbApi = new TmdbApi("e688e37074adf33cd49e7960d8705601");
            TmdbMovies tmdbMovies = tmdbApi.getMovies();
            MovieResultsPage movieResultsPage = tmdbMovies.getPopularMovies("english", 1);
            List<MovieDb> movieDbList = movieResultsPage.getResults();
            String[] posterPathArray = new String[movieDbList.size()];
            MovieDb movieDb;
            for (int i = 0; i < movieDbList.size(); i++) {
                movieDb = movieDbList.get(i);
                posterPathArray[i] = "http://image.tmdb.org/t/p/w185/" + movieDb.getPosterPath();
            }
            mMovieAdapter.setData(posterPathArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mMovieAdapter.notifyDataSetChanged();
            //((BaseAdapter)((GridView)getActivity().findViewById(R.id.grid_view_movies)).getAdapter()).notifyDataSetChanged();
        }


    }

}
