import styled from "styled-components";

const FooterDiv = styled.div`
  background-color: #348f8f;
`;

export default function Footer() {
  return (
    <FooterDiv className="bg text-white mt-auto">
      <div className="container py-3 text-center">
        Designed by DreamBuilder&copy;. All rights reserved.
      </div>
    </FooterDiv>
  );
}
