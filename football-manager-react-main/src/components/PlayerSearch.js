import { Box, Button, TextField } from "@mui/material";
import React from "react";
import { useForm } from "react-hook-form";
import { useQuery } from "react-query";
import { searchPlayers } from "../api";
import PlayerList from "./PlayerList";

const PlayerSearch = ({ onPlayerSelect }) => {
  const { register, handleSubmit, watch } = useForm();
  const name = watch("name", "");

  const {
    data: playersData,
    refetch,
    isLoading,
    isError,
  } = useQuery(
    ["players", name],
    () => searchPlayers({ name }),
    {
      enabled: false,
    }
  );

  const onSubmit = () => {
    refetch();
  };

  const players = playersData?.data?.players || [];

  return (
    <Box>
      <form onSubmit={handleSubmit(onSubmit)}>
        <TextField
          {...register("name")}
          label="Search Player"
          variant="outlined"
          fullWidth
          sx={{ marginBottom: 2 }}
        />
        <Button type="submit" variant="contained" color="primary" fullWidth>
          Search
        </Button>
      </form>

      {/* Pass isLoading, isError, and players to PlayerList */}
      <PlayerList
        players={players}
        isLoading={isLoading}
        isError={isError}
        onPlayerSelect={onPlayerSelect} 
      />
    </Box>
  );
};

export default PlayerSearch;
