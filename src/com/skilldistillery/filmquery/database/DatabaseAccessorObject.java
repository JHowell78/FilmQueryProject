package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " SELECT Id, release_year, language_id, rental_duration,  rental_rate,  length,  title,  description,  replacement_cost,  rating,  special_features FROM film WHERE Id  = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet findKeyWord = stmt.executeQuery();
			while (findKeyWord.next()) {
				film = new Film();
				film.setId(findKeyWord.getInt("Id"));
				film.setReleaseYear(findKeyWord.getInt("release_year"));
				film.setLanguageId(findKeyWord.getInt("language_id"));
				film.setRentalDuration(findKeyWord.getInt("rental_duration"));
				film.setRentalRate(findKeyWord.getInt("rental_rate"));
				film.setLength(findKeyWord.getInt("length"));
				film.setTitle(findKeyWord.getString("title"));
				film.setDescription(findKeyWord.getString("description"));
				film.setReplacementCost(findKeyWord.getDouble("replacement_cost"));
				film.setRating(findKeyWord.getString("rating"));
				film.setSpecialFeatures(findKeyWord.getString("special_features"));
				film.setActors(findActorsByFilmId(filmId));
				film.setLanguage(languageFromFilmID(filmId));
			}
			conn.close();
			stmt.close();
			findKeyWord.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;

	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		List<Actor> actorsIdList = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " SELECT actor.id,  actor.first_name, actor.last_name FROM actor JOIN film_actor on actor.id = film_actor.actor_id JOIN film on film.id = film_actor.film_id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet actorId = stmt.executeQuery();
			while (actorId.next()) {
				int actId = actorId.getInt("actor.id");
				String firstName = actorId.getString("actor.first_name");
				String lastName = actorId.getString("actor.last_name");
				Actor actor = new Actor(actId, firstName, lastName);
				actorsIdList.add(actor);
			}
			conn.close();
			stmt.close();
			actorId.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actorsIdList;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " SELECT Id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
				actor.setId(actorResult.getInt("Id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));
			}
			conn.close();
			stmt.close();
			actorResult.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Film> findByKeyword(String key) throws SQLException {
		List<Film> filmByKey = new ArrayList<Film>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE title LIKE ? OR description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + key + "%");
			stmt.setString(2, "%" + key + "%");
			ResultSet findKeyWord = stmt.executeQuery();
			while (findKeyWord.next()) {
				Film film = new Film();
				film.setId(findKeyWord.getInt("Id"));
				film.setTitle(findKeyWord.getString("title"));
				film.setDescription(findKeyWord.getString("description"));
				film.setReleaseYear(findKeyWord.getInt("release_year"));
				film.setLanguageId(findKeyWord.getInt("language_id"));
				film.setRentalDuration(findKeyWord.getInt("rental_duration"));
				film.setRentalRate(findKeyWord.getInt("rental_rate"));
				film.setLength(findKeyWord.getInt("length"));
				film.setReplacementCost(findKeyWord.getDouble("replacement_cost"));
				film.setRating(findKeyWord.getString("rating"));
				film.setSpecialFeatures(findKeyWord.getString("special_features"));
				film.setActors(findActorsByFilmId(film.getId()));
				film.setLanguage(languageFromFilmID(film.getId()));
				filmByKey.add(film);
			}
			conn.close();
			stmt.close();
			findKeyWord.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return filmByKey;
	}

	private String languageFromFilmID(int filmId) throws SQLException {
		String language = "";
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " SELECT film.language_id, language.name FROM film  JOIN language on film.language_id = language.id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet langFromId = stmt.executeQuery();
			while (langFromId.next()) {
				language = langFromId.getString("language.name");
			}
			conn.close();
			stmt.close();
			langFromId.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return language;
	}

}
