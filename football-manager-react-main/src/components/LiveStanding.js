// LiveStanding.jsx
import React from 'react';
import './styles/LeagueTable.css';

const LiveStanding = () => {
  const standings = [
    {
      position: 1,
      club: 'Tottenham Hotspur',
      mp: 8,
      w: 6,
      d: 2,
      l: 0,
      gf: 18,
      ga: 8,
      gd: 10,
      pts: 20,
      form: ['W', 'D', 'W', 'W', 'D'],
      next: 'Arsenal FC',
      status: 'Champions League',
    },
    {
      position: 2,
      club: 'Arsenal FC',
      mp: 8,
      w: 6,
      d: 2,
      l: 0,
      gf: 16,
      ga: 6,
      gd: 10,
      pts: 20,
      form: ['W', 'W', 'D', 'D', 'W'],
      next: 'Tottenham Hotspur',
      status: 'Champions League',
    },
    {
      position: 3,
      club: 'Manchester City',
      mp: 8,
      w: 6,
      d: 0,
      l: 2,
      gf: 17,
      ga: 6,
      gd: 11,
      pts: 18,
      form: ['W', 'L', 'W', 'W', 'L'],
      next: 'Brighton & Hove Albion',
      status: 'Champions League',
    },
    {
      position: 4,
      club: 'Liverpool FC',
      mp: 8,
      w: 5,
      d: 2,
      l: 1,
      gf: 18,
      ga: 9,
      gd: 9,
      pts: 17,
      form: ['W', 'D', 'L', 'W', 'W'],
      next: 'West Ham United',
      status: 'Champions League',
    },
    {
      position: 5,
      club: 'Aston Villa',
      mp: 8,
      w: 6,
      d: 0,
      l: 2,
      gf: 19,
      ga: 12,
      gd: 7,
      pts: 18,
      form: ['W', 'L', 'W', 'W', 'W'],
      next: 'Chelsea FC',
      status: 'Europa League',
    },
    {
      position: 6,
      club: 'Brighton & Hove Albion',
      mp: 8,
      w: 5,
      d: 1,
      l: 2,
      gf: 21,
      ga: 16,
      gd: 5,
      pts: 16,
      form: ['L', 'W', 'D', 'W', 'L'],
      next: 'Manchester City',
      status: '',
    },
    {
      position: 7,
      club: 'West Ham United',
      mp: 8,
      w: 4,
      d: 2,
      l: 2,
      gf: 15,
      ga: 12,
      gd: 3,
      pts: 14,
      form: ['D', 'W', 'L', 'D', 'W'],
      next: 'Liverpool FC',
      status: '',
    },
    {
      position: 8,
      club: 'Newcastle United',
      mp: 8,
      w: 4,
      d: 1,
      l: 3,
      gf: 20,
      ga: 9,
      gd: 11,
      pts: 13,
      form: ['W', 'L', 'W', 'L', 'W'],
      next: 'Crystal Palace',
      status: '',
    },
    {
      position: 9,
      club: 'Crystal Palace',
      mp: 8,
      w: 3,
      d: 3,
      l: 2,
      gf: 7,
      ga: 7,
      gd: 0,
      pts: 12,
      form: ['D', 'L', 'W', 'D', 'D'],
      next: 'Newcastle United',
      status: '',
    },
    {
      position: 10,
      club: 'Manchester United',
      mp: 8,
      w: 4,
      d: 0,
      l: 4,
      gf: 9,
      ga: 12,
      gd: -3,
      pts: 12,
      form: ['L', 'W', 'L', 'W', 'L'],
      next: 'Brentford FC',
      status: '',
    },
    {
      position: 11,
      club: 'Chelsea FC',
      mp: 8,
      w: 3,
      d: 2,
      l: 3,
      gf: 11,
      ga: 7,
      gd: 4,
      pts: 11,
      form: ['W', 'D', 'L', 'D', 'W'],
      next: 'Aston Villa',
      status: '',
    },
    {
      position: 12,
      club: 'Fulham FC',
      mp: 8,
      w: 3,
      d: 2,
      l: 3,
      gf: 8,
      ga: 13,
      gd: -5,
      pts: 11,
      form: ['L', 'D', 'W', 'L', 'D'],
      next: 'Everton FC',
      status: '',
    },
    {
      position: 13,
      club: 'Nottingham Forest',
      mp: 8,
      w: 2,
      d: 3,
      l: 3,
      gf: 8,
      ga: 11,
      gd: -3,
      pts: 9,
      form: ['D', 'L', 'D', 'L', 'W'],
      next: 'Wolverhampton Wanderers',
      status: '',
    },
    {
      position: 14,
      club: 'Wolverhampton Wanderers',
      mp: 8,
      w: 2,
      d: 2,
      l: 4,
      gf: 9,
      ga: 14,
      gd: -5,
      pts: 8,
      form: ['L', 'W', 'L', 'D', 'L'],
      next: 'Nottingham Forest',
      status: '',
    },
    {
      position: 15,
      club: 'Brentford FC',
      mp: 8,
      w: 1,
      d: 4,
      l: 3,
      gf: 10,
      ga: 12,
      gd: -2,
      pts: 7,
      form: ['D', 'L', 'D', 'L', 'D'],
      next: 'Manchester United',
      status: '',
    },
    {
      position: 16,
      club: 'Everton FC',
      mp: 8,
      w: 2,
      d: 1,
      l: 5,
      gf: 9,
      ga: 13,
      gd: -4,
      pts: 7,
      form: ['W', 'L', 'L', 'W', 'L'],
      next: 'Fulham FC',
      status: '',
    },
    {
      position: 17,
      club: 'Ipswich Town',
      mp: 8,
      w: 1,
      d: 1,
      l: 6,
      gf: 7,
      ga: 20,
      gd: -13,
      pts: 4,
      form: ['L', 'L', 'D', 'L', 'L'],
      next: 'Leicester City',
      status: 'Relegation',
    },
    {
      position: 18,
      club: 'AFC Bournemouth',
      mp: 8,
      w: 0,
      d: 3,
      l: 5,
      gf: 5,
      ga: 15,
      gd: -10,
      pts: 3,
      form: ['D', 'L', 'L', 'D', 'L'],
      next: 'Southampton FC',
      status: 'Relegation',
    },
    {
      position: 19,
      club: 'Southampton FC',
      mp: 8,
      w: 0,
      d: 1,
      l: 7,
      gf: 5,
      ga: 22,
      gd: -17,
      pts: 1,
      form: ['L', 'L', 'L', 'D', 'L'],
      next: 'AFC Bournemouth',
      status: 'Relegation',
    },
    {
      position: 20,
      club: 'Leicester City',
      mp: 8,
      w: 0,
      d: 0,
      l: 8,
      gf: 5,
      ga: 25,
      gd: -20,
      pts: 0,
      form: ['L', 'L', 'L', 'L', 'L'],
      next: 'Ipswich Town',
      status: 'Relegation',
    },
  ];

  return (
    <div className="league-table">
      <table>
        <thead>
          <tr>
            <th className="position-indicator"></th>
            <th>Position</th>
            <th>Club</th>
            <th>Played</th>
            <th>Won</th>
            <th>Drawn</th>
            <th>Lost</th>
            <th>GF</th>
            <th>GA</th>
            <th>GD</th>
            <th>Points</th>
            <th>Form</th>
            <th>Next</th>
          </tr>
        </thead>
        <tbody>
          {standings.map((team, index) => (
            <tr
              key={index}
              className={
                team.status === 'Champions League'
                  ? 'champions-league'
                  : team.status === 'Europa League'
                  ? 'europa-league'
                  : team.status === 'Relegation'
                  ? 'relegation'
                  : ''
              }
            >
              <td className="position-indicator"></td>
              <td>{team.position}</td>
              <td className="club-name">
                <img
                  src={require(`../assets/clubLogos/${team.club}.png`)}
                  alt={`${team.club} logo`}
                  className="club-logo"
                />
                {team.club}
              </td>
              <td>{team.mp}</td>
              <td>{team.w}</td>
              <td>{team.d}</td>
              <td>{team.l}</td>
              <td>{team.gf}</td>
              <td>{team.ga}</td>
              <td>{team.gd}</td>
              <td>{team.pts}</td>
              <td>
                {team.form.map((result, i) => (
                  <span
                    key={i}
                    className={
                      result === 'W'
                        ? 'form-won'
                        : result === 'D'
                        ? 'form-drawn'
                        : 'form-lost'
                    }
                  >
                    {result}
                  </span>
                ))}
              </td>
              <td>
                <img
                  src={require(`../assets/clubLogos/${team.next}.png`)}
                  alt={`${team.next} logo`}
                  className="next-logo"
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default LiveStanding;
