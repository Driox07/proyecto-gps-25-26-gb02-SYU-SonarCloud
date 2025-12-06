package com.gb02.syumsvc.utils;

import com.gb02.syumsvc.exceptions.UnexpectedErrorException;

public class Base64Img {
    
    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
    
    public static String saveB64(String b64, String nick){
        String sanitizedNick = sanitizeFilename(nick);
        String extension = b64.substring(b64.indexOf("/") + 1, b64.indexOf(";"));
        String base64Data = b64.substring(b64.indexOf(",") + 1);
        
        java.nio.file.Path baseDir = java.nio.file.Paths.get("src/main/resources/static/pfp").toAbsolutePath().normalize();
        java.nio.file.Path path = baseDir.resolve(sanitizedNick + "." + extension).normalize();
        
        // Verify the resolved path is still within the base directory
        if (!path.startsWith(baseDir)) {
            throw new UnexpectedErrorException("Invalid file path");
        }
        
        try {
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);
            java.nio.file.Files.createDirectories(path.getParent());
            java.nio.file.Files.write(path, decodedBytes);
        } catch (java.io.IOException e) {
            throw new UnexpectedErrorException("Error while trying to save profile picture: " + e.getMessage());
        }
        return extension;
    }

    public static String changeNick(String oldImagePath, String newNick){
        String sanitizedNewNick = sanitizeFilename(newNick);
        String extension = oldImagePath.substring(oldImagePath.lastIndexOf('.') + 1);
        
        // Define base directory
        java.nio.file.Path baseDir = java.nio.file.Paths.get("src/main/resources/static").toAbsolutePath().normalize();
        
        // Si oldImagePath ya es una ruta relativa (/pfp/...), la convertimos a absoluta
        String sourcePath = oldImagePath.startsWith("/") 
            ? "src/main/resources/static" + oldImagePath 
            : oldImagePath;
        java.nio.file.Path source = java.nio.file.Paths.get(sourcePath).toAbsolutePath().normalize();
        java.nio.file.Path target = baseDir.resolve("pfp/" + sanitizedNewNick + "." + extension).normalize();
        
        // Verify both paths are within the base directory
        if (!source.startsWith(baseDir) || !target.startsWith(baseDir)) {
            throw new UnexpectedErrorException("Invalid file path");
        }
        
        try {
            java.nio.file.Files.move(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "/pfp/" + sanitizedNewNick + "." + extension;
        } catch (java.io.IOException e) {
            throw new UnexpectedErrorException("Error while trying to rename profile picture: " + source.toString());
        }
    }
}
