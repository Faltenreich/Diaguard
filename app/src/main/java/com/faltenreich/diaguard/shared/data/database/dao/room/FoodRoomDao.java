package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.faltenreich.diaguard.shared.data.database.dao.BaseDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;

import java.util.List;

@Dao
public interface FoodRoomDao extends FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(List<Food> foodList);

    @Nullable
    @Query("SELECT * FROM Food WHERE _id = :id")
    Food getById(long id);

    @Nullable
    @Query("SELECT * FROM Food WHERE serverId = :serverId")
    Food getByServerId(String serverId);

    @Nullable
    @Query("SELECT * FROM Food WHERE name = :name")
    Food getByName(String name);

    @Query("SELECT * FROM Food WHERE serverId IS NULL AND labels IS NOT NULL ")
    List<Food> getCommon();

    @Query("SELECT * FROM Food WHERE serverId IS NULL AND labels IS NULL")
    List<Food> getCustom();

    @RawQuery
    List<Food> search(SupportSQLiteQuery query);

    default List<Food> search(
        String query,
        long page,
        boolean showCustomFood,
        boolean showCommonFood,
        boolean showBrandedFood
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM Food");
        builder.append(" WHERE deletedAt IS NULL");

        if (!StringUtils.isBlank(query)) {
            builder.append(" AND name LIKE '%").append(query).append("%'");
        }

        boolean hasFilter = false;
        if (showCustomFood) {
            builder.append(" AND ((serverId IS NULL AND labels IS NULL)");
            hasFilter = true;
        }
        if (showCommonFood) {
            String link = hasFilter ? " OR" : " (AND";
            builder.append(link).append(" (serverId IS NULL AND labels IS NOT NULL)");
            hasFilter = true;
        }
        if (showBrandedFood) {
            String link = hasFilter ? " OR" : " (AND";
            builder.append(link).append(" (serverId IS NOT NULL)");
        }
        builder.append(")");

        builder.append(" ORDER BY name COLLATE NOCASE ASC, updatedAt DESC");
        builder.append(" LIMIT ").append(BaseDao.PAGE_SIZE);
        builder.append(" OFFSET ").append(page * BaseDao.PAGE_SIZE);

        return search(new SimpleSQLiteQuery(builder.toString()));
    }

    @Delete
    int delete(Food object);

    @Delete
    int delete(List<Food> objects);

    // TODO: Check if necessary
    @Query("DELETE FROM Food")
    void deleteAll();
}
