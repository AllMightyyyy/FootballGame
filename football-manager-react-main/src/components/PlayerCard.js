// PlayerCard.js
import React from 'react';
import { Box } from '@mui/material';
import uclBackground from '../ucl_live.png';

const PlayerCard = ({ player, size = 'normal' }) => {
  const baseWidth = 300;
  const baseHeight = 500;

  const smallCardWidth = 100; 

  const scale = size === 'small' ? smallCardWidth / baseWidth : 1;

  const scaleValue = (value) => value * scale;

  return (
    <Box
      style={{
        backgroundImage: `url(${uclBackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        width: `${baseWidth * scale}px`,
        height: `${baseHeight * scale}px`,
        position: 'relative',
        overflow: 'hidden',
        margin: '0 auto',
      }}
    >
      {/* Player Image */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(150)}px`,
          top: `${scaleValue(150)}px`,
          transform: 'translate(-50%, -50%)',
          width: `${scaleValue(150)}px`,
          height: `${scaleValue(150)}px`,
        }}
      >
        <img
          src={player.playerFaceUrl}
          alt={player.shortName}
          draggable="false"
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            borderRadius: '50%',
          }}
        />
      </div>

      {/* Player Rating */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(37)}px`,
          top: `${scaleValue(75)}px`,
          fontSize: `${scaleValue(36)}px`,
          fontWeight: '700',
          color: 'white',
          textShadow: `${scaleValue(1)}px ${scaleValue(1)}px ${scaleValue(2)}px black`,
        }}
      >
        {player.overall}
      </div>

      {/* Player Position */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(39)}px`,
          top: `${scaleValue(110)}px`,
          fontSize: `${scaleValue(24)}px`,
          fontWeight: '700',
          color: 'white',
          textShadow: `${scaleValue(1)}px ${scaleValue(1)}px ${scaleValue(2)}px black`,
        }}
      >
        {player.positions.split(',')[0]}
      </div>

      {/* Nation Flag */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(40)}px`,
          top: `${scaleValue(140)}px`,
          width: `${scaleValue(32)}px`,
          height: `${scaleValue(20)}px`,
        }}
      >
        <img
          src={player.nationFlagUrl}
          alt={player.nationality}
          draggable="false"
          style={{ width: '100%', height: '100%', objectFit: 'cover' }}
        />
      </div>

      {/* Club Logo */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(40)}px`,
          top: `${scaleValue(166)}px`,
          width: `${scaleValue(32)}px`,
          height: `${scaleValue(32)}px`,
        }}
      >
        <img
          src={player.clubLogoUrl}
          alt={player.club}
          draggable="false"
          style={{ width: '100%', height: '100%', objectFit: 'cover' }}
        />
      </div>

      {/* Player Name */}
      <div
        style={{
          position: 'absolute',
          left: '50%',
          top: `${scaleValue(270)}px`,
          transform: 'translateX(-50%)',
          fontSize: `${scaleValue(20)}px`,
          fontWeight: '800',
          color: 'white',
          textShadow: `${scaleValue(1)}px ${scaleValue(1)}px ${scaleValue(2)}px black`,
          width: `${scaleValue(260)}px`,
          textAlign: 'center',
          whiteSpace: 'nowrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        }}
      >
        {player.shortName}
      </div>

      {/* Player Attributes */}
      {/* Left Column */}
      <div
        style={{
          position: 'absolute',
          left: `${scaleValue(60)}px`,
          top: `${scaleValue(320)}px`,
          color: 'white',
          textShadow: `${scaleValue(1)}px ${scaleValue(1)}px ${scaleValue(2)}px black`,
          fontSize: `${scaleValue(18)}px`,
          fontWeight: '600',
          lineHeight: `${scaleValue(1.5)}`,
        }}
      >
        <div>PAC {player.pace}</div>
        <div>SHO {player.shooting}</div>
        <div>PAS {player.passing}</div>
      </div>

      {/* Right Column */}
      <div
        style={{
          position: 'absolute',
          right: `${scaleValue(60)}px`,
          top: `${scaleValue(320)}px`,
          color: 'white',
          textShadow: `${scaleValue(1)}px ${scaleValue(1)}px ${scaleValue(2)}px black`,
          fontSize: `${scaleValue(18)}px`,
          fontWeight: '600',
          lineHeight: `${scaleValue(1.5)}`,
          textAlign: 'right',
        }}
      >
        <div>DRI {player.dribbling}</div>
        <div>DEF {player.defending}</div>
        <div>PHY {player.physical}</div>
      </div>
    </Box>
  );
};

export default PlayerCard;
