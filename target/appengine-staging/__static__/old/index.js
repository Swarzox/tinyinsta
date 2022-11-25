'use strict';

const element = React.createElement;

function LikeButton(props) {
    const [liked,setLiked] = React.useState(false)

    if (liked) {
      return 'You liked this.';
    }

    return element(
      'button',
      { style:{color: "red"}, onClick: () => setLiked(true) },
      'Like'
    );
}

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(element(LikeButton));