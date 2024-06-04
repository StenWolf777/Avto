package org.example.repository;

import org.example.BotState;
import org.example.User;
import org.example.config.CustomDataSource;

import java.sql.*;

public class UserRepository {
    private final String findById = "SELECT * FROM users WHERE chat_id = ?";
    private final String updatePhoneNumber = "update users SET phone_number=? WHERE chat_id=?  ";
    private final String findBotStateById = "SELECT bot_state FROM users WHERE chat_id = ?";
    private final String updateById = "UPDATE users SET full_name=?, role=?, bot_state=?, grade = ? WHERE chat_id=?";
    private final String insertUser =
            "INSERT INTO users (chat_id, bot_state) VALUES(?,?)";
    private final String updateBotState = "UPDATE users SET bot_state=? WHERE chat_id=?";
    private final  String updateLanguage = "Update users Set language=? Where chat_id=?";

    public void updateBotState(long chatId, BotState botState) {
        try {
            Connection conn = CustomDataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(updateBotState);

            preparedStatement.setString(1, botState.name());
            preparedStatement.setLong(2, chatId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setUpdateLanguage(long chatId, String language ) {
        try {
            Connection conn = CustomDataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(updateLanguage);

            preparedStatement.setString(1,language);
            preparedStatement.setLong(2, chatId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public BotState getUserBotState(long chatId) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findBotStateById, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setObject(1, chatId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return BotState.valueOf(resultSet.getString("bot_state"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createUser(long chatId, BotState state) {
        try {
            Connection conn = CustomDataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(insertUser);

            preparedStatement.setLong(1, chatId);
            preparedStatement.setString(2, state.name());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(long chatId) {
        User user = new User();
        try {
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(findById);
            ps.setLong(1, chatId);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return null ;
            }
            user.setChatId(resultSet.getLong("chat_id"));
            user.setState(BotState.valueOf(resultSet.getString("bot_state")));
            user.setLanguage((resultSet.getString("language")));
            user.setPhoneNumber((resultSet.getString("phone_number")));
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPhoneNumber(Long chatId, String phoneNumber) {
        try {
            Connection conn = CustomDataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(updatePhoneNumber);

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setLong(2, chatId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
