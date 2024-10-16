import CloseIcon from "@mui/icons-material/Close";
import {
  Box,
  Button,
  Dialog,
  IconButton,
  List,
  TextField,
  Typography,
} from "@mui/material";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useQuery } from "react-query";
import { searchPlayers } from "../api";
import { useFormation } from "../contexts/FormationContext";
import Filters from "./Filters";
import PlayerCard from "./PlayerCard";
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
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [availablePositions, setAvailablePositions] = useState([]);
  const [selectedPosition, setSelectedPosition] = useState("");
  const { formation, updateFormation } = useFormation();
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  const {
    data: playersData,
    refetch,
    isLoading,
    isError,
  } = useQuery(
    ["players", searchTerm, filters],
    () => searchPlayers({ name: searchTerm, ...filters }),
    { enabled: false }
  );

  const handleAddToFormation = () => {
    if (selectedPosition) {
      updateFormation(selectedPosition, selectedPlayer);
      handleClosePlayerCard();
      handleClose();
      setSnackbarOpen(true);
    } else {
      alert("Please select a position");
    }
  };

  const onSubmit = () => {
    refetch();
  };

  const handlePlayerClick = (player) => {
    setSelectedPlayer(player);

    const playerPositions = player.positions
      .split(",")
      .map((pos) => pos.trim());

    let positionsInFormation = [];

    playerPositions.forEach((pos) => {
      switch (pos) {
        case "CB":
          if (!formation.LCB) positionsInFormation.push("LCB");
          if (!formation.RCB) positionsInFormation.push("RCB");
          break;
        case "CM":
          if (!formation.LCM) positionsInFormation.push("LCM");
          if (!formation.RCM) positionsInFormation.push("RCM");
          if (!formation.CM) positionsInFormation.push("CM");
          break;
        case "LB":
        case "RB":
        case "GK":
        case "LW":
        case "RW":
        case "ST":
          if (!formation[pos]) positionsInFormation.push(pos);
          break;
        default:
          break;
      }
    });
    onPlayerSelect(player, positionsInFormation); // ADD this to pass player and positions back to the parent
    handleClose(); // ADD this to close the overlay after player selection
  };

  const handleClosePlayerCard = () => {
    setSelectedPlayer(null);
    setSelectedPosition("");
    setAvailablePositions([]);
  };

  return (
    <>
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
              width: "300px",
              padding: "20px",
              backgroundColor: "#2e2e2e",
              borderRight: "1px solid #444",
            }}
          >
            <Filters onFilterChange={onFilterChange} />
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
                  width: "200px",
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
              {playersData && playersData.data.players.length > 0 ? (
                <List>
                  {playersData.data.players.map((player) => (
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
          </Box>
        </Box>
      </Dialog>

      {/*  PlayerCard Modal with Position Selection */}
      {selectedPlayer && (
        <Dialog
          open={true}
          onClose={handleClosePlayerCard}
          maxWidth="sm" // Set a maximum width for the dialog
          fullWidth
          sx={{
            "& .MuiDialog-paper": {
              backgroundColor: "#1e1e1e", // Set background color
              color: "#fff",
              padding: "20px",
              borderRadius: "8px",
            },
          }}
        >
          <IconButton
            aria-label="close"
            onClick={handleClosePlayerCard}
            sx={{ position: "absolute", top: 10, right: 10, color: "#fff" }}
          >
            <CloseIcon />
          </IconButton>

          {/* Player Card Display */}
          <Box sx={{ display: "flex", justifyContent: "center" }}>
            <PlayerCard player={selectedPlayer} />
          </Box>

          {/* Position Selection */}
          <Box sx={{ marginTop: "20px", textAlign: "center" }}>
            <Typography variant="h6" sx={{ marginBottom: "10px" }}>
              Select Position
            </Typography>
            <Button
              variant="contained"
              color="primary"
              onClick={handleAddToFormation}
              sx={{ width: "200px" }}
              disabled={!selectedPosition}
            >
              Add to Formation
            </Button>
          </Box>
        </Dialog>
      )}
    </>
  );
};

export default PlayerSearchOverlay;
