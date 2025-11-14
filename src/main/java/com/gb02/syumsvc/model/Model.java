package com.gb02.syumsvc.model;

import com.gb02.syumsvc.model.factory.TransactionFactory;
import com.gb02.syumsvc.model.factory.DAOFactory;
import com.gb02.syumsvc.model.dao.postgresql.PostgresqlTransactionFactory;
import com.gb02.syumsvc.exceptions.DupedEmailException;
import com.gb02.syumsvc.exceptions.DupedUsernameException;
import com.gb02.syumsvc.exceptions.SessionExpiredException;
import com.gb02.syumsvc.exceptions.SessionNotFoundException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.exceptions.UserNotFoundException;
import com.gb02.syumsvc.model.dao.UsuarioDAO;
import com.gb02.syumsvc.model.dao.postgresql.PostgresqlDAOFactory;
import com.gb02.syumsvc.model.dto.SesionDTO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;

public class Model {
    
    private static Model model = null;
    TransactionFactory tf;
    DAOFactory df;
    
    public static Model getModel(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public Model() {
        tf = new PostgresqlTransactionFactory();
        df = new PostgresqlDAOFactory();
    }

    public UsuarioDTO registrarUsuario(UsuarioDTO usuario) throws DupedEmailException, DupedUsernameException, UnexpectedErrorException {
        try {
            return tf.registrarUsuario(usuario);
        } catch (DupedEmailException e){
            throw e;
        } catch (DupedUsernameException e){
            throw e;
        } catch (UnexpectedErrorException e){
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in registrarUsuario: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in registrarUsuario: " + e.getMessage());
        }
    }

    public UsuarioDTO getUsuario(int idUsuario) throws UserNotFoundException, UnexpectedErrorException{
        try{
            UsuarioDAO usuarioDAO = df.getUsuarioDao();
            return usuarioDAO.obtainUsuario(idUsuario);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (UnexpectedErrorException e) {
            throw e;
        } catch (Exception e){
            throw new UnexpectedErrorException("Unexpected error in getUsuario: " + e.getMessage());
        }
    }
    
    public UsuarioDTO getUsuarioByNick(String nick) throws UserNotFoundException, UnexpectedErrorException{
        try{
            UsuarioDAO usuarioDAO = df.getUsuarioDao();
            return usuarioDAO.obtainUsuarioByNick(nick);
        } catch (UserNotFoundException unfe) {
            throw unfe;
        } catch (UnexpectedErrorException uee) {
            throw uee;
        } catch (Exception e){
            throw new UnexpectedErrorException("Unexpected error in getUsuarioByNick: " + e.getMessage());
        }
    }

    public UsuarioDTO getUsuarioByMail(String mail) throws UserNotFoundException, UnexpectedErrorException{
        try{
            UsuarioDAO usuarioDAO = df.getUsuarioDao();
            return usuarioDAO.obtainUsuarioByMail(mail);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (UnexpectedErrorException e) {
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in getUsuario: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in getUsuario: " + e.getMessage());
        }
    }

    public void insertarSesion(SesionDTO sesion) throws UnexpectedErrorException {
        try{
            tf.crearSesion(sesion);
        } catch (UnexpectedErrorException e){
            System.out.println("Unexpected error in insertarSesion: " + e.getMessage());
            throw e;
        }
    }

    public SesionDTO getSessionByToken(String token) throws UnexpectedErrorException, SessionNotFoundException, SessionExpiredException {
        try{
            SesionDTO sesion = df.getSesionDao().obtainSesion(token);
            java.util.Date now = new java.util.Date();
            if(sesion.getFechaValidez().before(new java.sql.Date(now.getTime()))){
                throw new SessionExpiredException("Session has expired");
            }
            return sesion;
        } catch (SessionExpiredException e){
            throw e;
        } catch (SessionNotFoundException e){
            throw e;
        } catch (UnexpectedErrorException e){
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in getSessionByToken: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in getSessionByToken: " + e.getMessage());
        }
    }

    public boolean deleteSesion(int idSesion) throws UnexpectedErrorException, SessionNotFoundException {
        try{
            return df.getSesionDao().deleteSesion(idSesion);
        } catch (SessionNotFoundException e){
            throw e;
        } catch (UnexpectedErrorException e){
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in deleteSession: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in deleteSession: " + e.getMessage());
        }
    }

    public boolean deleteUsuario(int idUsuario) throws UnexpectedErrorException, UserNotFoundException {
        try{
            return df.getUsuarioDao().deleteUsuario(idUsuario);
        } catch (UserNotFoundException e){
            throw e;
        } catch (UnexpectedErrorException e){
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in deleteUsuario: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in deleteUsuario: " + e.getMessage());
        }
    }

    public boolean updateUsuario(int idUsuario, UsuarioDTO usuario) throws UnexpectedErrorException, UserNotFoundException {
        try{
            return df.getUsuarioDao().modifyUsuario(idUsuario, usuario);
        } catch (UserNotFoundException e){
            throw e;
        } catch (UnexpectedErrorException e){
            throw e;
        } catch (Exception e){
            System.out.println("Unexpected error in updateUsuario: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error in updateUsuario: " + e.getMessage());
        }
    }

}
