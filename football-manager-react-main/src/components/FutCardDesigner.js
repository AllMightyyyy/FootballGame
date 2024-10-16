import React, { useState } from 'react';
import Draggable from 'react-draggable';
import cardBackground from '../ucl_live.png';
import playerImage from '../cristiano.png';
import flagImage from '../protugal.png';
import clubImage from '../juventus-logo.png'; 

const FUTCardDesigner = () => {
  const [elements, setElements] = useState({
    playerImage: { x: 110, y: 150, width: 150, height: 150 },
    rating: { x: 20, y: 20 },
    positionText: { x: 20, y: 80 },
    flagImage: { x: 20, y: 140 },
    clubImage: { x: 20, y: 200 },
    overallAbility: { x: 20, y: 260 },
    playerName: { x: 100, y: 320 },
    attributes: {
      PAC: { x: 40, y: 400 },
      SHO: { x: 40, y: 430 },
      PAS: { x: 40, y: 460 },
      DRI: { x: 220, y: 400 },
      DEF: { x: 220, y: 430 },
      PHY: { x: 220, y: 460 },
    },
    styleLines: [],
  });

  const handleStop = (e, data, key, subKey) => {
    if (subKey) {
      setElements((prev) => ({
        ...prev,
        [key]: {
          ...prev[key],
          [subKey]: { x: data.x, y: data.y },
        },
      }));
    } else {
      setElements((prev) => ({
        ...prev,
        [key]: { ...prev[key], x: data.x, y: data.y },
      }));
    }
  };

  const handleResizeImage = (e) => {
    const newWidth = e.target.value;
    setElements((prev) => ({
      ...prev,
      playerImage: { ...prev.playerImage, width: newWidth, height: newWidth },
    }));
  };

  const logPositions = () => {
    console.log('Element positions and styles:', elements);
  };

  return (
    <div>
      <div
        style={{
          width: 350,
          height: 500,
          border: '1px solid black',
          position: 'relative',
          backgroundImage: `url(${cardBackground})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          margin: '0 auto',
        }}
      >
        {/* Draggable Player Image */}
        <Draggable
          defaultPosition={{ x: elements.playerImage.x, y: elements.playerImage.y }}
          onStop={(e, data) => handleStop(e, data, 'playerImage')}
        >
          <img
            src={playerImage}
            alt="Player"
            style={{
              width: elements.playerImage.width,
              height: elements.playerImage.height,
              position: 'absolute',
              cursor: 'move',
            }}
          />
        </Draggable>

        {/* Draggable Rating */}
        <Draggable
          defaultPosition={elements.rating}
          onStop={(e, data) => handleStop(e, data, 'rating')}
        >
          <div
            style={{
              position: 'absolute',
              fontSize: 40,
              fontWeight: 'bold',
              cursor: 'move',
              color: 'white',
              textShadow: '2px 2px 4px black',
            }}
          >
            99
          </div>
        </Draggable>

        {/* Draggable Position Text */}
        <Draggable
          defaultPosition={elements.positionText}
          onStop={(e, data) => handleStop(e, data, 'positionText')}
        >
          <div
            style={{
              position: 'absolute',
              fontSize: 28,
              fontWeight: 'bold',
              cursor: 'move',
              color: 'white',
              textShadow: '2px 2px 4px black',
            }}
          >
            CB
          </div>
        </Draggable>

        {/* Draggable Flag Image */}
        <Draggable
          defaultPosition={elements.flagImage}
          onStop={(e, data) => handleStop(e, data, 'flagImage')}
        >
          <img
            src={flagImage}
            alt="Flag"
            style={{
              width: 50,
              height: 30,
              position: 'absolute',
              cursor: 'move',
            }}
          />
        </Draggable>

        {/* Draggable Club Image */}
        <Draggable
          defaultPosition={elements.clubImage}
          onStop={(e, data) => handleStop(e, data, 'clubImage')}
        >
          <img
            src={clubImage}
            alt="Club"
            style={{
              width: 50,
              height: 50,
              position: 'absolute',
              cursor: 'move',
            }}
          />
        </Draggable>

        {/* Draggable Overall Ability Number */}
        <Draggable
          defaultPosition={elements.overallAbility}
          onStop={(e, data) => handleStop(e, data, 'overallAbility')}
        >
          <div
            style={{
              position: 'absolute',
              fontSize: 36,
              fontWeight: 'bold',
              cursor: 'move',
              color: 'white',
              textShadow: '2px 2px 4px black',
            }}
          >
            99
          </div>
        </Draggable>

        {/* Draggable Player Name */}
        <Draggable
          defaultPosition={elements.playerName}
          onStop={(e, data) => handleStop(e, data, 'playerName')}
        >
          <div
            style={{
              position: 'absolute',
              fontSize: 24,
              fontWeight: 'bold',
              cursor: 'move',
              color: 'white',
              textShadow: '1px 1px 2px black',
            }}
          >
            Cristiano Ronaldo
          </div>
        </Draggable>

        {/* Draggable Attributes */}
        {Object.entries(elements.attributes).map(([attr, pos]) => (
          <Draggable
            key={attr}
            defaultPosition={pos}
            onStop={(e, data) => handleStop(e, data, 'attributes', attr)}
          >
            <div
              style={{
                position: 'absolute',
                fontSize: 20,
                fontWeight: 'bold',
                cursor: 'move',
                color: 'white',
                textShadow: '1px 1px 2px black',
              }}
            >
              {attr}: 99
            </div>
          </Draggable>
        ))}

        {/* Draggable Style Lines */}
        {elements.styleLines.map((line, index) => (
          <Draggable
            key={index}
            defaultPosition={line}
            onStop={(e, data) => {
              const newLines = [...elements.styleLines];
              newLines[index] = { x: data.x, y: data.y };
              setElements((prev) => ({
                ...prev,
                styleLines: newLines,
              }));
            }}
          >
            <div
              style={{
                position: 'absolute',
                width: '100%',
                height: '2px',
                backgroundColor: 'white',
                cursor: 'move',
              }}
            />
          </Draggable>
        ))}
      </div>

      <div style={{ textAlign: 'center', marginTop: 20 }}>
        <input
          type="range"
          min="100"
          max="200"
          value={elements.playerImage.width}
          onChange={handleResizeImage}
          style={{ marginBottom: 10 }}
        />
        <div>
          <button onClick={logPositions} style={{ marginRight: 10 }}>
            Log Positions
          </button>
          <button
            onClick={() =>
              setElements((prev) => ({
                ...prev,
                styleLines: [...prev.styleLines, { x: 0, y: 350 }],
              }))
            }
          >
            Add Style Line
          </button>
        </div>
      </div>
    </div>
  );
};

export default FUTCardDesigner;
