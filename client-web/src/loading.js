import styled from '@emotion/styled';
import { keyframes} from '@emotion/core';
import { size } from 'polished';

import { ReactComponent as Logo } from './assets/logo.svg';

const spin = keyframes`
  to {
    transform: rotate(360deg);
  }
`;

const Loading = styled(Logo)(size(64), {
  display: 'block',
  margin: 'auto',
  fill: '#d8d9e0',
  path: {
    transformOrigin: 'center',
    animation: `${spin} 1s linear infinite`,
  },
});

export default Loading;
