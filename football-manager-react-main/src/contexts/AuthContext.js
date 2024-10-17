// src/contexts/AuthContext.js

import React, { createContext, useState, useEffect } from 'react';
import api from '../api';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    user: null,
    token: null,
    team: null, // New field to store team information
  });

  useEffect(() => {
    // Check for token in localStorage on mount
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user'));
    console.log('AuthContext useEffect: Token:', token, 'User:', user);
    if (token && user) {
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team: user.team || null, // Initialize team from user data
      });
      // Set the token in the api instance
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, []);

  const login = async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      const { token, user } = response.data;
      console.log('Login successful. Response data:', response.data);
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team: user.team || null,
      });
      // Set the token in the api instance
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      // Fetch additional user info, including team status
      const teamResponse = await api.get('/auth/me');
      const userData = teamResponse.data.user;
      const team = teamResponse.data.team;

      if (team) {
        setAuth({
          isAuthenticated: true,
          user: userData,
          token,
          team, // Add team information to context
        });
        return { success: true, hasTeam: true };
      } else {
        return { success: true, hasTeam: false };
      }
    } catch (error) {
      console.error('Login failed:', error);
      return { success: false, message: error.response?.data?.message || 'Login failed.' };
    }
  };

  const register = async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      const { token, user } = response.data;
      console.log('Registration successful. Response data:', response.data);
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team: user.team || null,
      });
      // Set the token in the api instance
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      // Fetch additional user info, including team status
      const teamResponse = await api.get('/auth/me');
      const userInfo = teamResponse.data.user;
      const team = teamResponse.data.team;

      if (team) {
        setAuth({
          isAuthenticated: true,
          user: userInfo,
          token,
          team,
        });
        return { success: true, hasTeam: true };
      } else {
        return { success: true, hasTeam: false };
      }
    } catch (error) {
      console.error('Registration failed:', error);
      return { success: false, message: error.response?.data?.message || 'Registration failed.' };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setAuth({
      isAuthenticated: false,
      user: null,
      token: null,
      team: null,
    });
    // Remove the token from the api instance
    delete api.defaults.headers.common['Authorization'];
  };

  const updateTeam = (team) => {
    setAuth((prev) => ({
      ...prev,
      team,
      user: { ...prev.user, team }, // Update user data with team
    }));
    // Update user in localStorage
    const updatedUser = { ...auth.user, team };
    localStorage.setItem('user', JSON.stringify(updatedUser));
  };

  return (
    <AuthContext.Provider value={{ auth, login, register, logout, updateTeam }}>
      {children}
    </AuthContext.Provider>
  );
};
