import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface periodData {
  period: string;
  visits: number;
}

interface PeriodVisitsTableProps {
  data: periodData[];
}

const options = {
  responsive: true,
  plugins: {
    legend: {
      position: "top" as const,
    },
    title: {
      display: true,
      text: "방문자 수 현황",
    },
  },
};

const PeriodVisitsTable: React.FC<PeriodVisitsTableProps> = ({ data }) => {
  return (
    <div
      className="table-responsive"
      style={{ maxHeight: "400px", overflowY: "auto" }}
    >
      <table className="table table-striped table-bordered">
        <thead className="thead-dark">
          <tr>
            <th>기간</th>
            <th>방문횟수</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index}>
              <td>{row.period}</td>
              <td>{row.visits}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

function MangeVisitor() {
  const [data, setData] = useState<periodData[]>([
    { period: "2021 Q1", visits: 100 },
    { period: "2021 Q2", visits: 150 },
    { period: "2021 Q3", visits: 200 },
    { period: "2021 Q4", visits: 250 },
    { period: "2022 Q1", visits: 300 },
    { period: "2022 Q2", visits: 350 },
    { period: "2022 Q3", visits: 400 },
    { period: "2022 Q4", visits: 450 },
    { period: "2023 Q1", visits: 500 },
    { period: "2023 Q2", visits: 550 },
    { period: "2023 Q3", visits: 600 },
    // Add more rows as needed
  ]);

  const [chartData, setChartData] = useState({
    labels: data.map((e) => e.period),
    datasets: [
      {
        label: "Dataset 1",
        data: data.map((e) => e.visits),
        borderColor: "rgb(255, 99, 132)",
        backgroundColor: "rgba(255, 99, 132, 0.5)",
      },
    ],
  });
  return (
    <>
      <Header />
      <div className="container mt-5 mb-5">
        <h1 className="text-center">방문자 수 현황</h1>
        <hr />
        <div className="d-flex justify-content-end mb-2">
          <button
            className="btn btn-primary mx-0"
            onClick={() => console.log("일간")}
          >
            일간
          </button>
          <button
            className="btn btn-primary mx-1"
            onClick={() => console.log("주간")}
          >
            주간
          </button>
          <button
            className="btn btn-primary mx-0"
            onClick={() => console.log("월간")}
          >
            월간
          </button>
        </div>
        <Line data={chartData} />
        <div className="row mt-5">
          <h3>기간별 방문자</h3>
          <hr></hr>
          <div className="col-md-6">
            <PeriodVisitsTable data={data} />
          </div>
          <div className="col-md-6">
            <PeriodVisitsTable data={data} />
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default MangeVisitor;
