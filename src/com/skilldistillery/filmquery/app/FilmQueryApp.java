package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

//	private void test() {
//		Film film = null;
//		try {
//			film = db.findFilmById(59);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println(film);
//	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);
		startUserInterface(input);
		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		boolean menu = true;
		Film movie = new Film();
		String userChoice = "";
		int filmId = 0;
		do {

			System.out.println("Welcome to Film Query" + "\n");
			System.out.println("Please choose one of the following: " + "\n");
			System.out.println("1. Choose a film by it's ID #: ");
			System.out.println("2. Search for films by a keyword: ");
			System.out.println("3. Exit");

			userChoice = input.next();
			switch (userChoice) {

			case "1":
				System.out.println("Please enter a film ID # (1 - 1000)");
				try {
					filmId = input.nextInt();
					movie = db.findFilmById(filmId);

					if (movie != null) {
						System.out.println(movie + "\n");
					} else {
						System.out.println("\n" + "Unable to find a movie with that ID #" + "\n"
								+ "Please enter a valid Id # (1 - 1000) \n");
					}
				} catch (InputMismatchException e) {
					input.nextLine();
					System.out.println("\n" + "Unable to find a movie with that ID # " + "\n"
							+ "Please enter a valid Id # (1 - 1000) \n");
				}
				break;

			case "2":
				System.out.println("Search for a film using a keyword" + "\n");
				try {
					String key = input.next();
					List<Film> filmKeyWord = db.findByKeyword(key);

					if (filmKeyWord.size() > 0) {
						for (Film film : filmKeyWord) {
							System.out.println(film + "\n");
						}
					} else {
						System.out.println("Unable to find any matches using that keyword. Please try again: \n");
					}
				} catch (InputMismatchException e) {
					input.nextLine();
					System.out.println("Invalid input, please try again: ");
				}
				break;
			case "3":
				System.out.println("Goodbye");
				menu = false;
				break;
			default:
				System.out.println("Please enter a valid number" + "\n");
			}
		} while (menu);
	}
}
