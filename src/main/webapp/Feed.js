export default function Feed({setCurrentProfil, pageFeed}) {
	
	const nbElt = 1;
	const [page, setPage] = React.useState(0);
	const [feed, setFeed] = React.useState([]);
	const e = React.createElement
	
	React.useEffect(() => {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/feedUser")
			.then((res) => {
				if(res.status != 200) {
					return;
				}
				return res.text();
			}).then((data) => {
				if(data) {
					const arr = JSON.parse(data).items;
					const posts = [];
					for(let i = 0; i < arr.length; i++) {
						const prop = arr[i].properties;
						posts[i] = prop;
					}
					setFeed(posts);
				} else {
					setFeed([]);
				}
			});
	},[]);
	
	return (
		e("div", {className: "container mx-auto"},
			e("div", { className: "container mx-auto my-10 sm:px-20 flex justify-center gap-2"}, 
			feed.length > 0 ? 
			feed.map((img,i) => {
			  return(
				  i >= page*nbElt && i < (page+1)*nbElt ?
				  e("div", {className: "rounded overflow-hidden border w-full lg:w-6/12 md:w-6/12 bg-white mx-3 md:mx-0 lg:mx-0", key: img.id},
					e("div", {className: "w-full flex justify-between p-3"},
						e("div", {className: "flex"},
							e("div", {
							  className: "rounded-full h-8 w-8 bg-gray-500 flex items-center justify-center overflow-hidden cursor-pointer"
							}, e("img", {
							  src: "https://cdn-icons-png.flaticon.com/512/1/1247.png",
							  alt: "profilepic"
							})),
							e("span", {
							  className: "pt-1 ml-2 font-bold text-sm cursor-pointer",
							}, img.date)
						)
				  ),
					e("img", {
					  class: "w-full bg-cover",
					  src: img.url
					}),
					e("div", {
					  class: "px-3 pb-2"
					}, e("div", {
					  class: "pt-2"
					}, e("i", {
					  class: "far fa-heart cursor-pointer"
					}), e("span", {
					  class: "text-sm text-gray-400 font-medium"
					}, img.likes + " likes")), e("div", {
					  class: "pt-1"
					}, e("div", {
					  class: "mb-2 text-sm"
					}, e("span", {
					  class: "font-medium mr-2"
					}, "Description"), img.description)))
				  ) :
				  null
				  
			  )
			}) : e("p", null, "Fil d'actualité vide...")
			),
			feed.length > 0 && e("div", {className: "flex w-full justify-center gap-2 mb-12"},
				e("button", {className: "gg-arrow-left", onClick: () => setPage(page-1), disabled: page==0}, null),
				e("label", null, parseInt(page+1)+"/" + feed.length),
				e("button", {className: "gg-arrow-right", onClick: () => setPage(page+1), disabled: (page+1)*nbElt >= feed.length}, null)
			)
		)
	)
}