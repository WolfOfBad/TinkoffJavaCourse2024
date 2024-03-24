package edu.java.scrapper.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationsTest extends IntegrationTest {
    @Test
    public void chatTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM public.chat");

            String firstColumn = query.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = query.executeQuery().getMetaData().getColumnName(2);

            assertThat(firstColumn).isEqualTo("id");
            assertThat(secondColumn).isEqualTo("tg_chat_id");
        }
    }

    @Test
    public void linkTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM public.link");

            String firstColumn = query.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = query.executeQuery().getMetaData().getColumnName(2);
            String thirdColumn = query.executeQuery().getMetaData().getColumnName(3);

            assertThat(firstColumn).isEqualTo("id");
            assertThat(secondColumn).isEqualTo("uri");
            assertThat(thirdColumn).isEqualTo("last_update");
        }
    }

    @Test
    public void chatLinkConnectionTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM public.chat_link");

            String firstColumn = query.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = query.executeQuery().getMetaData().getColumnName(2);

            assertThat(firstColumn).isEqualTo("chat_id");
            assertThat(secondColumn).isEqualTo("link_id");
        }
    }

}
