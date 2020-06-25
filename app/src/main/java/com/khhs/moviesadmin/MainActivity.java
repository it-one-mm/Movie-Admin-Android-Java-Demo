package com.khhs.moviesadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khhs.moviesadmin.ui.SlideFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FragmentManager manager = getSupportFragmentManager();
    Fragment genresFrag = new GenresFragment();
    Fragment moviesFrag = new MoviesFragment();
    Fragment seriesFrag = new SeriesFragment();
    Fragment episodeFrag = new EpisodeFragment();
    Fragment slideFrag = new SlideFragment();
    Fragment activeFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        manager.beginTransaction().add(R.id.fragment_container, seriesFrag).hide(seriesFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, episodeFrag).hide(episodeFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, moviesFrag).hide(moviesFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, genresFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container,slideFrag).hide(slideFrag).commit();
        setTitle("Genres");
        activeFrag = genresFrag;

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFrag = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_genres:
                        selectedFrag = genresFrag;
                        setTitle("Genres");
                        break;
                    case R.id.action_movies:
                        selectedFrag = moviesFrag;
                        setTitle("Movies");
                        break;
                    case R.id.action_series:
                        selectedFrag = seriesFrag;
                        setTitle("Series");
                        break;
                    case R.id.action_episode:
                        selectedFrag = episodeFrag;
                        setTitle("Episodes");
                        break;
                    case R.id.action_slide:
                        selectedFrag = slideFrag;
                        setTitle("Slides");
                }
                manager.beginTransaction().hide(activeFrag).show(selectedFrag).commit();
                activeFrag = selectedFrag;

                return true;
            }
        });
    }
}
