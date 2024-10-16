// FormationContext.js
import React, { createContext, useContext, useState } from 'react';

const FormationContext = createContext();

export const useFormation = () => {
  const context = useContext(FormationContext);
  if (!context) {
    throw new Error('useFormation must be used within a FormationProvider');
  }
  return context;
};

export const FormationProvider = ({ children }) => {
  const [formation, setFormation] = useState({
    GK: null,
    LB: null,
    LCB: null,
    RCB: null,
    RB: null,
    CDM: null,
    LCM: null,
    CM: null,
    RCM: null,
    LW: null,
    RW: null,
    ST: null,
  });

  const updateFormation = (position, player) => {
    console.log('Updating Formation:', position, player);
    setFormation((prev) => ({ ...prev, [position]: player }));
  };

  const removePlayer = (position) => {
    setFormation((prev) => ({ ...prev, [position]: null }));
  };

  return (
    <FormationContext.Provider value={{ formation, updateFormation, removePlayer }}>
      {children}
    </FormationContext.Provider>
  );
};
