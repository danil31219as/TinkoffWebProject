package com.example.tinkoffproject.repositories

import com.example.tinkoffproject.models.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.sql.ResultSet

@Repository
class CrudRepository {
    val conn = DriverManager.getConnection("jdbc:sqlite:./actions.db")

    init {
        conn.createStatement()
            .execute("CREATE TABLE IF NOT EXISTS Operation (\n" +
                    "'id' INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "         'amount' INTEGER DEFAULT 0,\n" +
                    "         'date' INTEGER NOT NULL,\n" +
                    "         'action' INTEGER NOT NULL,\n" +
                    "         'category_id' INTEGER NOT NULL,\n" +
                    "         'way' TEXT NOT NULL,\n" +
                    "         'comment' TEXT\n" +
                    ")")
        conn.createStatement()
            .execute("CREATE TABLE IF NOT EXISTS Category (\n" +
                    "'id' INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "'title' TEXT NOT NULL,\n" +
                    "'action' INTEGER NOT NULL,\n" +
                    "'description' TEXT\n" +
                    ")")
        conn.createStatement()
            .execute("INSERT INTO Operation (amount, date, action, category_id, way, comment) VALUES (100, 1620148846, -1, 21, 'cash', 'Купил шорты на лето')")
        conn.createStatement()
            .execute("INSERT INTO Operation (amount, date, action, category_id, way, comment) VALUES (1000, 1620149000, 1, 2, 'card', 'Пришли проценты по вкладу')")
        arrayListOf("Премии",
            "Проценты",
            "Зарплата",
            "Подарки",
            "Продажа",
            "Другое").forEach {
            conn.createStatement()
                .execute("INSERT INTO Category (title, action, description) VALUES ('$it', 1, '')")
        }
        arrayListOf("Транспорт",
            "Такси",
            "Парковка",
            "Заправка",
            "Техобслуживание",
            "Продукты",
            "Рестораны",
            "Кафе",
            "Мобильная связь",
            "Вода",
            "Электричество",
            "Газ",
            "Интернет",
            "Аренда",
            "Одежда",
            "Обувь",
            "Аксессуары",
            "Электроника",
            "Дружба и отношения",
            "Кино",
            "Игры",
            "Развлечения",
            "Путешествия",
            "Здоровье",
            "Врач",
            "Спорт",
            "Аптека",
            "Гигиена",
            "Пожертвования",
            "Свадьба",
            "Семья",
            "Дети",
            "Ремонт",
            "Дом",
            "Питомцы",
            "Образование",
            "Курсы",
            "Книги",
            "Инвестиции",
            "Бизнес",
            "Страховка",
            "Налоги",
            "Съём",
            "Другое").forEach {
            conn.createStatement()
                .execute("INSERT INTO Category (title, action, description) VALUES ('$it', -1, '')")
        }

    }

    fun findByEqual(table: String, fields: List<String>, values: List<String>): ResultSet {
        val query = "select * from $table where ${fields.map{"$it = ?"}.joinToString(" and ") }"
        val preparedStatement = conn.prepareStatement(query)
        values.forEachIndexed{
                index, value ->
            preparedStatement.setString(index + 1, value)
        }
        return preparedStatement.executeQuery()

    }

    fun findAll(table: String): ResultSet {
        val query = "select * from $table"
        val preparedStatement = conn.prepareStatement(query)
        return preparedStatement.executeQuery()
    }

    fun findByRange(
        table: String,
        field: String,
        start: String,
        end: String,
    ): ResultSet {
        val query = "select * from $table where $field >= ? and $field <= ?"
        val preparedStatement = conn.prepareStatement(query)
        preparedStatement.setString(1, start)
        preparedStatement.setString(2, end)
        return preparedStatement.executeQuery()
    }

    fun insert(table: String, columns: List<String>, values: List<String>) {
        val query = "INSERT INTO $table (${columns.joinToString()}) VALUES (${
            (1..values.size).toMutableList().map { "?" }.joinToString()
        })"
        val preparedStatement = conn.prepareStatement(query)
        values.forEachIndexed { index, value ->
            preparedStatement.setString(index + 1,
                value)
        }
        preparedStatement.execute()
    }

    fun update(
        table: String,
        columns: List<String>,
        values: List<String>,
        id: String,
    ) {
        val query =
            "UPDATE $table SET ${columns.joinToString { "$it = ?" }} WHERE id = ?"
        println(query)
        val preparedStatement = conn.prepareStatement(query)
        values.forEachIndexed { index, value ->
            preparedStatement.setString(index + 1,
                value)
        }
        preparedStatement.setString(values.size + 1, id)
        preparedStatement.execute()
    }

    fun delete(table: String, id: String) {
        val query = "DELETE FROM $table WHERE id = ?"
        val preparedStatement = conn.prepareStatement(query)
        preparedStatement.setString(1, id)
        preparedStatement.execute()
    }
}