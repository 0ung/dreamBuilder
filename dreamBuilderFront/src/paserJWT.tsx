import base64 from "base-64";

const handleJWT = (jwt: string) => {
  const paylaod = jwt.substring(jwt.indexOf(".") + 1, jwt.lastIndexOf("."));
  const decodeInfo = base64.decode(paylaod);
  const json = JSON.parse(decodeInfo);

  return json;
};

export default handleJWT;
