// src/components/PlayerSearchOverlay.js

import CloseIcon from "@mui/icons-material/Close";
import {
  Box,
  Button,
  Dialog,
  IconButton,
  List,
  TextField,
  Typography,
  ListItem,
  TablePagination,
} from "@mui/material";
import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useQuery } from "react-query";
import { searchPlayers } from "../api";
import Filters from "./Filters";
import PlayerListItem from "./PlayerListItem";

const PlayerSearchOverlay = ({
  open,
  handleClose,
  filters,
  onFilterChange,
  onPlayerSelect,
}) => {
  const { register, handleSubmit } = useForm();
  const [searchTerm, setSearchTerm] = useState("");
  const [page, setPage] = useState(0); // 0-based index
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const mergedFilters = {
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
    name: searchTerm,
    ...filters,
  };

  const {
    data: playersData,
    refetch,
    isLoading,
    isError,
  } = useQuery(
    ["players", mergedFilters, page, rowsPerPage],
    () =>
      searchPlayers({
        ...mergedFilters,
        page: page + 1, // API is 1-based
        size: rowsPerPage,
      }),
    {
      enabled: open, // Fetch only when overlay is open
      keepPreviousData: true,
    }
  );

  const onSubmit = () => {
    setPage(0); // Reset to first page on new search
    refetch();
  };

  const handlePlayerClick = (player) => {
    onPlayerSelect(player);
    handleClose();
  };

  const handleFilterChange = (updatedFilters) => {
    onFilterChange(updatedFilters);
    setPage(0);
    refetch();
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
    refetch();
  };

  const handleRowsPerPageChange = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
    refetch();
  };

  useEffect(() => {
    if (open) {
      refetch();
    }
  }, [filters, searchTerm, open, refetch]);

  return (
    <Dialog fullScreen open={open} onClose={handleClose}>
      <Box
        sx={{
          display: "flex",
          backgroundColor: "#1e1e1e",
          color: "#fff",
          height: "100%",
        }}
      >
        {/* Filters Section */}
        <Box
          sx={{
            width: "450px",
            padding: "20px",
            backgroundColor: "#2e2e2e",
            borderRight: "1px solid #444",
          }}
        >
          <Filters
            filters={filters}
            onFilterChange={handleFilterChange}
            onSearch={handleSubmit(onSubmit)}
          />
        </Box>

        {/* Search and Player List Section */}
        <Box
          sx={{
            flex: 1,
            padding: "20px",
            overflowY: "auto",
            position: "relative",
          }}
        >
          <IconButton
            aria-label="close"
            onClick={handleClose}
            sx={{ position: "absolute", top: 10, right: 10, color: "#fff" }}
          >
            <CloseIcon />
          </IconButton>

          <Box sx={{ textAlign: "center", marginBottom: "20px" }}>
            <Typography variant="h4" color="#fff" gutterBottom>
              Search Player
            </Typography>
            <TextField
              {...register("name")}
              label="Enter player name"
              variant="outlined"
              fullWidth
              sx={{
                backgroundColor: "#333",
                input: { color: "#fff" },
                marginBottom: 2,
                "& .MuiOutlinedInput-root": {
                  "& fieldset": {
                    borderColor: "#555",
                  },
                  "&:hover fieldset": {
                    borderColor: "#888",
                  },
                  "&.Mui-focused fieldset": {
                    borderColor: "#fff",
                  },
                },
                "& .MuiInputLabel-root": {
                  color: "#aaa",
                },
                "& .MuiInputLabel-root.Mui-focused": {
                  color: "#fff",
                },
                "& .MuiOutlinedInput-input": {
                  color: "#fff",
                  padding: "10px",
                },
              }}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <Button
              onClick={handleSubmit(onSubmit)}
              variant="contained"
              color="primary"
              sx={{
                backgroundColor: "#487748",
                color: "#fff",
                marginBottom: "16px",
                width: "100%",
                maxWidth: "200px",
              }}
            >
              Search
            </Button>
          </Box>

          <Box
            sx={{
              padding: "20px",
              maxHeight: "60vh",
              overflowY: "auto",
              backgroundColor: "#1e1e1e",
            }}
          >
            {isLoading && <div>Loading players...</div>}
            {isError && <div>Error loading players</div>}
            {playersData && playersData.players && playersData.players.length > 0 ? (
              <List>
                {playersData.players.map((player) => (
                  <PlayerListItem
                    key={player.id}
                    player={player}
                    onClick={handlePlayerClick}
                  />
                ))}
              </List>
            ) : (
              <div>No players found</div>
            )}
          </Box>

          {/* Pagination Controls */}
          {playersData && playersData.totalItems > rowsPerPage && (
            <Box sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}>
              <TablePagination
                component="div"
                count={playersData.totalItems}
                page={page}
                onPageChange={handlePageChange}
                rowsPerPage={rowsPerPage}
                onRowsPerPageChange={handleRowsPerPageChange}
                rowsPerPageOptions={[10, 20, 50]}
                labelRowsPerPage="Players per page"
              />
            </Box>
          )}
        </Box>
      </Box>
    </Dialog>
  );
};

export default PlayerSearchOverlay;
