import React from 'react';
import Draggable from 'react-draggable';

const DraggableItem = ({ children, itemName, onDragStop }) => {
  return (
    <Draggable onStop={(e, data) => onDragStop(e, data, itemName)}>
      <div style={{ position: 'absolute' }}>
        {children}
      </div>
    </Draggable>
  );
};

export default DraggableItem;
