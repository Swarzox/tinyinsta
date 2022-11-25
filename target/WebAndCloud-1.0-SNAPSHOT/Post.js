export default function Post({currentProfil, name, userId}) {
	
	const e = React.createElement
	const [post, setPost] = React.useState([]);
	const [userLikes, setUserLikes] = React.useState([]);
	const likesCount = React.useRef([]);
	
	React.useEffect( () => {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/userPost/"+currentProfil)
			.then((response) => {
				console.log(response);
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					const json = JSON.parse(data).items;
					const posts = [];
					json.forEach((e, i) => {
						const val = e.properties;
						const fetchPost = {url: val.url, date: val.date, key: val.id, likes: val.likes, description: val.description}
						posts[i] = fetchPost;
					});
					setPost(posts);	
				} else {
					setPost([]);	
				}
			});
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/userLikes/"+userId)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					const json = JSON.parse(data).items;
					const likes = [];
					json.forEach((e, i) => {
						likes[i] = e;
					});
					setUserLikes(likes);	
				} else {
					setUserLikes([]);	
				}
			});
	}, [currentProfil]);
	
	function handleLike(key, i) {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/likePost/"+key)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					setUserLikes([key, ...userLikes]);
					const val = parseInt(likesCount.current[i].textContent)
					likesCount.current[i].innerHTML = val+1 + " j'aime"
				}
			});
	}
	
	function handleDislike(key, i) {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/unlikePost/"+key)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					var index = userLikes.indexOf(key);
					console.log(userLikes.splice(index,1))
					setUserLikes(userLikes.splice(index, 1));
					const val = parseInt(likesCount.current[i].textContent)
					likesCount.current[i].innerHTML = val-1 + " j'aime"
				}
			});
	}
	
	return (
		e("div", {className: "flex"},
			e("div", { className: "my-10 mx-auto flex flex-col items-center gap-4"}, 
			//e("button", {onClick: () => console.log(userLikes)}, "ifergern"),
			post.length > 0 ? 
			post.map((img,i) => {
			  return(
				  e("div", {className: "rounded overflow-hidden border w-full lg:w-6/12 md:w-6/12 bg-white mx-3 md:mx-0 lg:mx-0"},
					e("div", {className: "w-full flex justify-between p-3"},
						e("div", {className: "flex"},
							e("div", {
							  className: "rounded-full h-8 w-8 bg-gray-500 flex items-center justify-center overflow-hidden"
							}, e("img", {
							  src: "https://cdn-icons-png.flaticon.com/512/1/1247.png",
							  alt: "profilepic"
							})),
							e("span", {
							  className: "pt-1 ml-2 font-bold text-sm"
							}, currentProfil == userId ? "Votre profil" : name)
						)
				  ),
					e("img", {
					  className: "w-full bg-cover",
					  src: img.url
					}),
					e("div", {
					  className: "px-3 pb-2"
					}, e("div", {
					  className: "pt-2",
					}, e("img", {
					  src: userLikes.includes(img.key) ? "https://cdn-icons-png.flaticon.com/512/833/833472.png" : "https://cdn-icons-png.flaticon.com/512/833/833300.png",
					  className: "w-6 h-6",
					  onClick: () => userLikes.includes(img.key) ? handleDislike(img.key, i) : handleLike(img.key, i)
					}), e("span", {
					  className: "text-sm text-gray-400 font-medium",
					  ref: el => likesCount.current[i] = el,
					}, img.likes + " j'aime")), 
					e("div", {
					  className: "pt-1"
					}, e("div", {
					  className: "mb-2 text-sm"
					}, e("span", {
					  className: "font-medium mr-2"
					}, "Description"), img.description)))
				  )
				  
			  )
			}) : e("p", {className: "bg-gray-100 rounded-2xl text-sm p-2"}, "Aucune photo")
			)
		)
	)
}