// Filters.js
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Button,
  Checkbox,
  Divider,
  FormControlLabel,
  TextField as MuiTextField,
  Switch,
  Typography,
} from "@mui/material";
import { styled } from "@mui/system";
import React, { useState } from "react";

// Define a styled TextField component with shared styles
const TextField = styled(MuiTextField)({
  backgroundColor: "#2e2e2e",
  borderRadius: "4px",
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
});

// Main Filters component
const Filters = ({ onFilterChange, onSearch }) => {
  const [filters, setFilters] = useState({
    rating: [40, 120],
    position: [],
    league: [],
    club: [],
    nation: [],
    height: [150, 215],
    weight: [40, 120],
    excludeSelected: {
      position: false,
      league: false,
      club: false,
      nation: false,
    },
  });

  const [expanded, setExpanded] = useState(false);

  const handleAccordionChange = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };

  const handleNumberFieldChange = (filterName, index, value) => {
    const updatedValues = [...filters[filterName]];
    updatedValues[index] = value;
    const updatedFilters = { ...filters, [filterName]: updatedValues };
    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const handleCheckboxChange = (filterName, value) => {
    const updatedFilters = {
      ...filters,
      [filterName]: filters[filterName].includes(value)
        ? filters[filterName].filter((v) => v !== value)
        : [...filters[filterName], value],
    };
    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const handleSwitchChange = (category, value) => {
    const updatedFilters = {
      ...filters,
      excludeSelected: {
        ...filters.excludeSelected,
        [category]: value,
      },
    };
    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const handleSelectAllChange = (group, checked) => {
    const updatedFilters = { ...filters };
    const positionsToToggle = groupedPositions[group];

    if (checked) {
      // Add all if not already present
      updatedFilters.position = [
        ...new Set([...filters.position, ...positionsToToggle]),
      ];
    } else {
      // Remove all
      updatedFilters.position = filters.position.filter(
        (pos) => !positionsToToggle.includes(pos)
      );
    }

    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const groupedPositions = {
    Defense: ["GK", "CB", "LB", "RB", "RWB", "LWB"],
    Midfield: ["CM", "CDM", "CAM", "LM", "RM"],
    Attack: ["ST", "CF", "LW", "RW", "LF", "RF"],
  };

  const handleSearchChange = (filterName, event) => {
    setFilters({ ...filters, [filterName]: event.target.value });
    onFilterChange({ ...filters, [filterName]: event.target.value });
  };

  return (
    <Box
      sx={{
        backgroundColor: "#1e1e1e",
        padding: "16px",
        borderRadius: "8px",
        color: "#fff",
      }}
    >
      <Typography variant="h6" gutterBottom>
        Filters
      </Typography>

      <Button
        variant="contained"
        onClick={onSearch}
        sx={{
          backgroundColor: "#487748",
          color: "#fff",
          marginBottom: "16px",
          width: "100%",
        }}
      >
        Search Players
      </Button>

      <Accordion
        expanded={expanded === "rating"}
        onChange={handleAccordionChange("rating")}
        sx={{ backgroundColor: "#2b472b", color: "#fff" }}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon sx={{ color: "#fff" }} />}
          aria-controls="rating-content"
          id="rating-header"
        >
          <Typography>Ratings</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Box sx={{ display: "flex", gap: 2 }}>
            <TextField
              label="Minimum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.rating[0]}
              onChange={(e) =>
                handleNumberFieldChange("rating", 0, Number(e.target.value))
              }
            />
            <TextField
              label="Maximum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.rating[1]}
              onChange={(e) =>
                handleNumberFieldChange("rating", 1, Number(e.target.value))
              }
            />
          </Box>
        </AccordionDetails>
      </Accordion>

      <Accordion
        expanded={expanded === "position"}
        onChange={handleAccordionChange("position")}
        sx={{ backgroundColor: "#2b472b", color: "#fff" }}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon sx={{ color: "#fff" }} />}
          aria-controls="position-content"
          id="position-header"
        >
          <Typography>Position</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <FormControlLabel
            control={
              <Switch
                checked={filters.excludeSelected.position}
                onChange={(e) =>
                  handleSwitchChange("position", e.target.checked)
                }
                sx={{ color: "#487748" }}
              />
            }
            label="Exclude selected"
          />
          <Divider sx={{ my: 1 }} />
          {Object.keys(groupedPositions).map((category) => (
            <Box key={category} sx={{ marginBottom: "10px" }}>
              <FormControlLabel
                control={
                  <Checkbox
                    onChange={(e) =>
                      handleSelectAllChange(category, e.target.checked)
                    }
                    sx={{ color: "#487748" }}
                  />
                }
                label={`Select All ${category}`}
              />
              <Typography variant="subtitle1" sx={{ color: "#73be73", mt: 1 }}>
                {category}
              </Typography>
              <Box sx={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
                {groupedPositions[category].map((position) => (
                  <FormControlLabel
                    key={position}
                    control={
                      <Checkbox
                        checked={filters.position.includes(position)}
                        onChange={() =>
                          handleCheckboxChange("position", position)
                        }
                        sx={{ color: "#487748" }}
                      />
                    }
                    label={position}
                  />
                ))}
              </Box>
            </Box>
          ))}
        </AccordionDetails>
      </Accordion>

      <Accordion
        expanded={expanded === "league"}
        onChange={handleAccordionChange("league")}
        sx={{ backgroundColor: "#2b472b", color: "#fff" }}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon sx={{ color: "#fff" }} />}
          aria-controls="league-content"
          id="league-header"
        >
          <Typography>League</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TextField
            placeholder="Search leagues"
            variant="outlined"
            size="small"
            fullWidth
            sx={{ marginBottom: "8px" }}
            onChange={(e) => handleSearchChange("league", e)}
          />
          <FormControlLabel
            control={
              <Switch
                checked={filters.excludeSelected.league}
                onChange={(e) => handleSwitchChange("league", e.target.checked)}
                sx={{ color: "#487748" }}
              />
            }
            label="Exclude selected"
          />
          <Divider sx={{ my: 1 }} />
          {["Premier League", "La Liga", "Bundesliga"].map((league) => (
            <FormControlLabel
              key={league}
              control={
                <Checkbox
                  checked={filters.league.includes(league)}
                  onChange={() => handleCheckboxChange("league", league)}
                  sx={{ color: "#487748" }}
                />
              }
              label={league}
            />
          ))}
        </AccordionDetails>
      </Accordion>

      <Accordion
        expanded={expanded === "height"}
        onChange={handleAccordionChange("height")}
        sx={{ backgroundColor: "#2b472b", color: "#fff" }}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon sx={{ color: "#fff" }} />}
          aria-controls="height-content"
          id="height-header"
        >
          <Typography>Height</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Box sx={{ display: "flex", gap: 2 }}>
            <TextField
              label="Minimum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.height[0]}
              onChange={(e) =>
                handleNumberFieldChange("height", 0, Number(e.target.value))
              }
            />
            <TextField
              label="Maximum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.height[1]}
              onChange={(e) =>
                handleNumberFieldChange("height", 1, Number(e.target.value))
              }
            />
          </Box>
        </AccordionDetails>
      </Accordion>

      <Accordion
        expanded={expanded === "weight"}
        onChange={handleAccordionChange("weight")}
        sx={{ backgroundColor: "#2b472b", color: "#fff" }}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon sx={{ color: "#fff" }} />}
          aria-controls="weight-content"
          id="weight-header"
        >
          <Typography>Weight</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Box sx={{ display: "flex", gap: 2 }}>
            <TextField
              label="Minimum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.weight[0]}
              onChange={(e) =>
                handleNumberFieldChange("weight", 0, Number(e.target.value))
              }
            />
            <TextField
              label="Maximum"
              type="number"
              variant="outlined"
              size="small"
              value={filters.weight[1]}
              onChange={(e) =>
                handleNumberFieldChange("weight", 1, Number(e.target.value))
              }
            />
          </Box>
        </AccordionDetails>
      </Accordion>
    </Box>
  );
};

export default Filters;
