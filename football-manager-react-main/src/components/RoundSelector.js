import React, { useState } from "react";
import styled from "styled-components";

const RoundSelector = ({ onRoundChange }) => {
  const [activeRound, setActiveRound] = useState(38);

  const onNumberClick = (num) => {
    setActiveRound(num);
    onRoundChange(num);
  };

  const renderRounds = (firstRound, lastRound) => {
    let nums = [];
    for (let i = firstRound; i <= lastRound; i++) {
      nums.push(
        <RoundNumber
          key={i}
          num={i}
          active={activeRound === i}
          onNumberClick={onNumberClick}
        />
      );
    }
    return nums;
  };

  return (
    <div style={{ margin: "2em 0 1em" }}>
      <div style={{ marginBottom: ".5em" }}>{renderRounds(1, 19)}</div>
      <div>{renderRounds(20, 38)}</div>
    </div>
  );
};

export default RoundSelector;

const RoundNumber = ({ num, active, onNumberClick }) => (
  <RoundNumberWrapper active={active} onClick={() => onNumberClick(num)}>
    {num}
  </RoundNumberWrapper>
);

const RoundNumberWrapper = styled.div`
  display: inline-block;
  width: 2em;
  ${({ active }) => active && "font-weight: 700; transform: scale(1.5);"};
  transition: 0.5s;
  cursor: pointer;
`;
