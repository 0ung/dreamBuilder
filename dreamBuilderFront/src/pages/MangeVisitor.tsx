import React, { useEffect, useState } from "react";
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
import fetcher from "../fetcher";
import {
  VISITED_DAILY,
  VISITED_MONTHLY,
  VISITED_WEEKLY,
} from "../constants/api_constants";

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
  const [data, setData] = useState<periodData[]>([]);
  const [kindsOfData, setKindsOfData] = useState("일간");

  const handleDaily = async () => {
    const response = await fetcher.get(VISITED_DAILY);
    setData(response.data);
    setKindsOfData("일간");
  };

  const handleWeekly = async () => {
    const response = await fetcher.get(VISITED_WEEKLY);
    setData(response.data);
    setKindsOfData("주간");
  };

  const handleMonthly = async () => {
    const response = await fetcher.get(VISITED_MONTHLY);
    setData(response.data);
    setKindsOfData("월간");
  };

  useEffect(() => {
    setChartData({
      labels: data.map((e) => e.period),
      datasets: [
        {
          label: kindsOfData,
          data: data.map((e) => e.visits),
          borderColor: "rgb(255, 99, 132)",
          backgroundColor: "rgba(255, 99, 132, 0.5)",
        },
      ],
    });
  }, [data]);

  useEffect(() => {
    handleDaily();
  }, []);

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
          <button className="btn btn-primary mx-0" onClick={handleDaily}>
            일간
          </button>
          <button className="btn btn-primary mx-1" onClick={handleWeekly}>
            주간
          </button>
          <button className="btn btn-primary mx-0" onClick={handleMonthly}>
            월간
          </button>
        </div>
        <Line data={chartData} />
        <div className="container my-5">
          <div className="d-flex flex-column align-items-center">
            <h3 className="mb-4">{`${kindsOfData}별 방문자`}</h3>
            <hr className="w-100" />
            <div className="col-md-6 mt-4">
              <PeriodVisitsTable data={data} />
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default MangeVisitor;
