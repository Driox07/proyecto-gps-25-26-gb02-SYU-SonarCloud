package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;

import com.gb02.syumsvc.exceptions.DupedEmailException;
import com.gb02.syumsvc.exceptions.DupedUsernameException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.model.dao.UsuarioDAO;
import com.gb02.syumsvc.model.dto.SesionDTO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;
import com.gb02.syumsvc.model.factory.DAOFactory;
import com.gb02.syumsvc.model.factory.TransactionFactory;

public class PostgresqlTransactionFactory implements TransactionFactory {

    DAOFactory daoFactory;

    public PostgresqlTransactionFactory() {
        daoFactory = new PostgresqlDAOFactory();
    }

    @Override
    public Connection getConnection() {
        return PostgresqlConnector.connect();
    }

    @Override
    public void transactionRollback(){
        try{
            getConnection().rollback();
        }catch(Exception e){
            System.err.println("Error during rollback: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuario) {
        try {
            UsuarioDAO usuarioDao = daoFactory.getUsuarioDao();
            int idRegistrado = usuarioDao.insertUsuario(usuario);
            UsuarioDTO registrado = usuarioDao.obtainUsuario(idRegistrado);
            if(registrado != null){
                System.out.println("User registered successfully.");
                getConnection().commit();
                return registrado;
            }
            System.out.println("Error registering user.");
            transactionRollback();
            throw new UnexpectedErrorException("Unexpected error while registering user: coudn't recover registered user.");
        } catch (DupedUsernameException due){
            transactionRollback();
            throw due;
        } catch (DupedEmailException dee){
            transactionRollback();
            throw dee;
        } catch (Exception e){
            transactionRollback();
            throw new UnexpectedErrorException("Unexpected error while registering user: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarUsuario(int idUsuario) {
        PostgresqlUsuarioDAO usuarioDAO = new PostgresqlUsuarioDAO();
        return usuarioDAO.deleteUsuario(idUsuario);
    }

    @Override
    public boolean modificarUsuario(int idUsuario, UsuarioDTO usuario) {
        PostgresqlUsuarioDAO usuarioDAO = new PostgresqlUsuarioDAO();
        return usuarioDAO.modifyUsuario(idUsuario, usuario);
    }
    
    @Override
    public boolean crearSesion(SesionDTO sesion) {
        try{
            PostgresqlSesionDAO sesionDAO = new PostgresqlSesionDAO();
            sesionDAO.insertSesion(sesion);
            getConnection().commit();
            return true;
        }catch(Exception e){
            System.err.println("Error creating session (PostgresqlTransactionFactory)");
            System.err.println("Reason: " + e.getMessage());
            transactionRollback();
            return false;
        }
    }

}
