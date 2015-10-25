/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * TokenProcessHelper is responsible for handling all token related functionality.
 * The methods in this class are synchronized to protect token processing from
 * multiple threads.  Servlet containers are allowed to return a different
 * HttpSession object for two threads accessing the same session so it is not
 * possible to synchronize on the session.
 * </p>
 * <p>Copy TokenProcessor.java from Struts 1.1</p>
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class TokenProcessHelper {

    /**
     * The session attributes key under which our transaction token is stored,
     * if it is used.
     */
    public static final String TRANSACTION_TOKEN_KEY = "net.ymate.framework.core.support.TRANSACTION_TOKEN";

    /**
     * The property under which a transaction token is reported.
     */
    public static final String TOKEN_KEY = "net.ymate.framework.core.support.TOKEN";

    /**
     * The singleton instance of this class.
     */
    private static TokenProcessHelper instance = new TokenProcessHelper();

    /**
     * The timestamp used most recently to generate a token value.
     */
    private long previous;

    /**
     * Protected constructor for TokenProcessHelper.  Use TokenProcessHelper.getInstance()
     * to obtain a reference to the processor.
     */
    protected TokenProcessHelper() {
        super();
    }

    /**
     * @return Retrieves the singleton instance of this class.
     */
    public static TokenProcessHelper getInstance() {
        return instance;
    }

    public synchronized boolean isTokenValid(HttpServletRequest request) {
        return this.isTokenValid(request, false);
    }

    public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {
        // Retrieve the current session for this request
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        // Retrieve the transaction token from this session, and
        // reset it if requested
        String saved = (String) session.getAttribute(TRANSACTION_TOKEN_KEY);
        if (saved == null) {
            return false;
        }
        if (reset) {
            this.resetToken(request);
        }
        // Retrieve the transaction token included in this request
        String token = request.getParameter(TOKEN_KEY);
        return token != null && saved.equals(token);
    }

    /**
     * Reset the saved transaction token in the user's session.  This
     * indicates that transactional token checking will not be needed on the
     * next request that is submitted.
     *
     * @param request The servlet request we are processing
     */
    public synchronized void resetToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(TRANSACTION_TOKEN_KEY);
    }

    /**
     * Save a new transaction token in the user's current session, creating a
     * new session if necessary.
     *
     * @param request The servlet request we are processing
     */
    public synchronized void saveToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = generateToken(request);
        if (token != null) {
            session.setAttribute(TRANSACTION_TOKEN_KEY, token);
        }
    }

    /**
     * Generate a new transaction token, to be used for enforcing a single
     * request for a particular transaction.
     *
     * @param request The request we are processing
     * @return a new transaction token
     */
    public synchronized String generateToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return generateToken(session.getId());
    }

    /**
     * Generate a new transaction token, to be used for enforcing a single
     * request for a particular transaction.
     *
     * @param id a unique Identifier for the session or other context in which
     *           this token is to be used.
     * @return a new transaction token with id
     */
    public synchronized String generateToken(String id) {
        try {
            long current = System.currentTimeMillis();

            if (current == previous) {
                current++;
            }

            previous = current;

            byte[] now = Long.toString(current).getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(id.getBytes());
            md.update(now);

            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Convert a byte array to a String of hexadecimal digits and return it.
     *
     * @param buffer The byte array to be converted
     */
    private String toHex(byte[] buffer) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (byte aBuffer : buffer) {
            sb.append(Character.forDigit((aBuffer & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(aBuffer & 0x0f, 16));
        }
        return sb.toString();
    }
}
