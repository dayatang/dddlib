package vo;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Produces(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public interface MenuApplication {

    /**
	 * 闂佸搫绉烽‖鍐ㄧ暤娓氾拷鍤曟繝濠傚暙缁�┉D闂佸吋鍎抽崲鑼躲亹閸ヮ剚鍤曟繝濠傚暙缁�菐閸ワ絽澧插ù纭锋嫹
	 * param : menuId 闂佸吋瀵х划灞界暦閻戠摵
	 */
    @GET()
    @Path("/MenuApplication/getMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/getMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/getMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/getMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/getMenu/{menuId}")
    public MenuVO getMenu(@PathParam("menuId") @PathParam("menuId") @PathParam("menuId") @PathParam("menuId") @PathParam("menuId") Long menuId) throws Exception;

    /**
	 * 婵烇絽娲︾换鍌炴偤閵娾晜鍤曟繝濠傚暙缁�拷
	 * param : menuVO 闂佸吋瀵х划灞界暦閻斿吋鍎嶉柛鏇ㄥ亾閹风兘鎮楅悽娈挎敯闁芥牭鎷�	 */
    @POST()
    @Path("/MenuApplication/saveMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/saveMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/saveMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/saveMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/saveMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    public MenuVO saveMenu(MenuVO menuVO) throws Exception;

    /**
	 * 婵烇絽娴傞崰妤呭极婵傚憡鍤曟繝濠傚暙缁�拷
	 * param : menuVO 闂佸吋瀵х划灞界暦閻斿吋鍎嶉柛鏇ㄥ亾閹风兘鎮楅悽娈挎敯闁芥牭鎷�	 */
    @POST()
    @Path("/MenuApplication/updateMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/updateMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/updateMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/updateMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/updateMenu")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    public void updateMenu(MenuVO menuVO) throws Exception;

    /**
	 * 闂佸憡甯炴繛锟解枍鎼淬劍鍤曟繝濠傚暙缁�拷
	 * param : menuId 闂佸吋瀵х划灞界暦閻戠摵
	 */
    @GET()
    @Path("/MenuApplication/removeMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/removeMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/removeMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/removeMenu/{menuId}")
    @GET()
    @Path("/MenuApplication/removeMenu/{menuId}")
    public void removeMenu(@PathParam("menuId") @PathParam("menuId") @PathParam("menuId") @PathParam("menuId") @PathParam("menuId") Long menuId) throws Exception;

    /**
	 * 闂佸搫琚崕鎾敋濡ゅ懎绠ラ柨鐔诲Г缁犳帡鎮峰▎鎰濠㈢櫢鎷�	 */
    @GET()
    @Path("/MenuApplication/findAllMenu")
    @GET()
    @Path("/MenuApplication/findAllMenu")
    @GET()
    @Path("/MenuApplication/findAllMenu")
    @GET()
    @Path("/MenuApplication/findAllMenu")
    @GET()
    @Path("/MenuApplication/findAllMenu")
    public List<MenuVO> findAllMenu() throws Exception;

    @POST()
    @Path("/MenuApplication/assign")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/assign")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/assign")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/assign")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    @POST()
    @Path("/MenuApplication/assign")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
    public void assign(MenuVO parent, MenuVO child) throws Exception;
}
