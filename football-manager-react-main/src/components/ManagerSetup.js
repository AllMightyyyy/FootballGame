// ManagerSetup.js
import React from "react";
import { Box, Button, TextField, Typography } from "@mui/material";

const ManagerSetup = ({ managerName, onManagerNameChange, onNext }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "20px",
        backgroundColor: "#2e2e2e",
        padding: "30px",
        borderRadius: "8px",
      }}
    >
      <Typography variant="h5" sx={{ color: "#fff" }}>
        Enter Your Manager Name
      </Typography>
      <TextField
        value={managerName}
        onChange={(e) => onManagerNameChange(e.target.value)}
        placeholder="Manager Name"
        variant="outlined"
        sx={{
          backgroundColor: "#1e1e1e",
          color: "#fff",
          input: { color: "#fff" },
          "& .MuiOutlinedInput-root": {
            "& fieldset": { borderColor: "#444" },
            "&:hover fieldset": { borderColor: "#888" },
            "&.Mui-focused fieldset": { borderColor: "#fff" },
          },
        }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={onNext}
        sx={{ backgroundColor: "#487748" }}
        disabled={!managerName}
      >
        Next
      </Button>
    </Box>
  );
};

export default ManagerSetup;
