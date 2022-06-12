package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

import org.joda.time.DateTime;

import java.util.List;

@Dao
public interface FoodEatenRoomDao extends FoodEatenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(FoodEaten foodEaten);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(List<FoodEaten> foodEatenList);

    @Query("SELECT * FROM FoodEaten WHERE mealId = :mealId ORDER BY createdAt")
    List<FoodEaten> getByMeal(long mealId);

    @Query("SELECT * FROM FoodEaten WHERE foodId = :foodId ORDER BY createdAt")
    List<FoodEaten> getByFood(long foodId);

    /*
    TODO: Inner join to Meal and Entry when entities have been migrated to Room
    public List<FoodEaten> getAll(Interval interval) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = EntryDao.getInstance().getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, interval.getStart())
                .and().le(Entry.Column.DATE, interval.getEnd());
            QueryBuilder<Meal, Long> queryBuilderMeal = MeasurementDao.getInstance(Meal.class).getQueryBuilder();
            return getQueryBuilder().join(queryBuilderMeal.join(queryBuilderEntry)).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
    */
    @Query("SELECT * FROM FoodEaten WHERE createdAt > :start AND createdAt < :end ORDER BY createdAt")
    List<FoodEaten> getBetween(DateTime start, DateTime end);

    // TODO: Check if distinct selection is working
    @Query("SELECT * FROM FoodEaten GROUP BY foodId ORDER BY createdAt LIMIT :count")
    List<FoodEaten> getLatest(long count);

    @Query("SELECT COUNT(foodId) FROM FoodEaten WHERE foodId = :foodId")
    long countByFood(long foodId);

    @Delete
    void delete(FoodEaten foodEaten);
}
