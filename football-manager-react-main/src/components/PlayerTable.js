import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TableSortLabel,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useQuery } from "react-query";
import { searchPlayers } from "../api";

const PlayerTable = ({ filters }) => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [order, setOrder] = useState("asc");
  const [orderBy, setOrderBy] = useState("overall");


  useEffect(() => {
    setPage(0); 
  }, [filters]);

  const {
    data: playersData,
    isLoading,
    isError,
  } = useQuery(
    ["players", page, rowsPerPage, orderBy, order, filters],
    () =>
      searchPlayers({
        page: page + 1,
        size: rowsPerPage,
        sortBy: orderBy,
        sortDirection: order,
        ...filters, 
      }),
    {
      keepPreviousData: true, 
    }
  );

  const handleSortRequest = (property) => {
    const isAsc = orderBy === property && order === "asc";
    setOrder(isAsc ? "desc" : "asc");
    setOrderBy(property);
  };

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
      }}
    >
      <TableContainer>
        <Table aria-label="player table">
          <TableHead>
            <TableRow>
              <TableCell>Picture</TableCell>
              <TableCell>
                <TableSortLabel
                  active={orderBy === "shortName"}
                  direction={order}
                  onClick={() => handleSortRequest("shortName")}
                >
                  Name
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={orderBy === "age"}
                  direction={order}
                  onClick={() => handleSortRequest("age")}
                >
                  Age
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={orderBy === "overall"}
                  direction={order}
                  onClick={() => handleSortRequest("overall")}
                >
                  Overall
                </TableSortLabel>
              </TableCell>
              <TableCell>Team</TableCell>
              <TableCell>Position</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {playersData?.data?.players.map((player) => (
              <TableRow key={player.id}>
                <TableCell>
                  <img
                    src={player.playerFaceUrl}
                    alt={player.shortName}
                    style={{ width: "50px", borderRadius: "50%" }}
                  />
                </TableCell>
                <TableCell>{player.shortName}</TableCell>
                <TableCell>{player.age}</TableCell>
                <TableCell>{player.overall}</TableCell>
                <TableCell>
                  {player.clubLogoUrl && (
                    <img src={player.clubLogoUrl} alt="Club Logo" />
                  )}
                </TableCell>
                <TableCell>{player.positions}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        component="div"
        count={playersData?.data?.totalItems || 0} 
        page={page}
        onPageChange={handleChangePage}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Paper>
  );
};

export default PlayerTable;
