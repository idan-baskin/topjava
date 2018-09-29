package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed userMealWithExceed : filteredWithExceeded) {
            System.out.println(userMealWithExceed);
        }
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

/*
        Реализация используя циклы:

        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            dateCaloriesMap.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> mealWithExceedsList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            boolean exceed = dateCaloriesMap.get(userMeal.getDate()) > caloriesPerDay;
            if (TimeUtil.isBetween(userMeal.getTime(), startTime, endTime)) {
                mealWithExceedsList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), exceed));
            }
        }
        return mealWithExceedsList;
*/

/*
        Реализация используя Stream API:
*/

        Map<LocalDate, Integer> dateCaloriesMap = mealList
                .stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return mealList
                .stream()
                .filter(temp -> TimeUtil.isBetween(temp.getTime(), startTime, endTime))
                .map(temp -> new UserMealWithExceed(temp.getDateTime(), temp.getDescription(), temp.getCalories(), dateCaloriesMap.get(temp.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
