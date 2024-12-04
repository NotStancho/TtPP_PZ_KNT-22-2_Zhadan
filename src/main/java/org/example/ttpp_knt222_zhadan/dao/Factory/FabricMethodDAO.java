package org.example.ttpp_knt222_zhadan.dao.Factory;

public class FabricMethodDAO {
    public static DAOFactory getDAOFactory(TypeDAO typeDAO){
        switch(typeDAO){
            case MYSQL -> {
                return new MySQLDAOFactory();
            }
            default -> throw new RuntimeException("Unsupported TypeDAO: " + typeDAO);
        }
    }

}
