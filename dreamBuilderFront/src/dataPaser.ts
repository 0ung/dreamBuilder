import { format } from "date-fns";

const formatDateTime = (dateTimeString: string) => {
  const date = new Date(dateTimeString);
  return format(date, 'yyyy-MM-dd HH:mm:ss');
};

export default formatDateTime;