import { Box, Button, TextField } from "@mui/material";
import React from "react";
import { useForm } from "react-hook-form";
import { useQuery } from "react-query";
import { searchPlayers } from "../api"; 
import PlayerList from "./PlayerList";

const PlayerSearch = () => {
  const { register, handleSubmit, watch } = useForm();
  const name = watch("name", "");

  const { data: playersData, refetch, isLoading, isError } = useQuery(
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
        <TextField {...register("name")} label="Search Player" variant="outlined" />
        <Button type="submit" variant="contained" color="primary">
          Search
        </Button>
      </form>

      {/* Pass isLoading, isError, and players to PlayerList */}
      <PlayerList players={players} isLoading={isLoading} isError={isError} />
    </Box>
  );
};

export default PlayerSearch;
