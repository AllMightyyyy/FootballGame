// src/components/PlayerTable.js

import React, { useEffect, useState } from "react";
import { Paper, Box, TablePagination } from "@mui/material";
import { useQuery } from "react-query";
import { searchPlayers } from "../api";
import PlayerListItem from "./PlayerListItem";

const PlayerTable = ({ filters, onPlayerSelect }) => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [order, setOrder] = useState("asc");
  const [orderBy, setOrderBy] = useState("overall");

  useEffect(() => {
    setPage(0);
  }, [filters]);

  const normalizedFilters = {
    rating: [0, 100],
    height: [150, 215],
    weight: [40, 120],
    position: [],
    league: [],
    club: [],
    nation: [],
    excludeSelected: {
      position: false,
      league: false,
      club: false,
      nation: false,
    },
    ...filters,
  };

  const {
    data: playersData,
    isLoading,
    isError,
  } = useQuery(
    ["players", page, rowsPerPage, orderBy, order, normalizedFilters],
    () =>
      searchPlayers({
        page: page + 1,
        size: rowsPerPage,
        sortBy: orderBy,
        sortOrder: order,
        ...normalizedFilters,
      }),
    {
      keepPreviousData: true,
    }
  );

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error fetching data</div>;

  return (
    <Paper
      sx={{
        width: "100%",
        overflow: "hidden",
        backgroundColor: "#1b1b1b",
        color: "#fff",
        padding: "16px",
      }}
    >
      <Box>
        {playersData?.players.map((player) => (
          <PlayerListItem
            key={player.id}
            player={player}
            onClick={onPlayerSelect}
          />
        ))}
      </Box>
      <TablePagination
        component="div"
        count={playersData?.totalItems || 0} 
        page={page}
        onPageChange={handleChangePage}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Paper>
  );
};

export default PlayerTable;
