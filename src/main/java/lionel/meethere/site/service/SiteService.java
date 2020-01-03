package lionel.meethere.site.service;

import lionel.meethere.paging.PageParam;
import lionel.meethere.site.dao.SiteMapper;
import lionel.meethere.site.entity.Site;
import lionel.meethere.site.param.SiteUpdateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    @Autowired
    SiteMapper  siteMapper;

    public void createSite(Site site){
        siteMapper.insertSite(site);
    }

    public void deleteSite(Integer id){
        siteMapper.deleteSite(id);
    }

    public void updateSite(SiteUpdateParam updateParam){
        Site site = siteMapper.getSite(updateParam.getId());
        if(updateParam.getName() != null) {
            site.setName(updateParam.getName());
        }
        if(updateParam.getLocation() !=null) {
            site.setLocation(updateParam.getLocation());
        }
        if(updateParam.getDescrption() != null) {
            site.setDescription(updateParam.getDescrption());
        }
        if(updateParam.getRent() != null) {
            site.setRent(updateParam.getRent());
        }
        if(updateParam.getImage() != null) {
            site.setImage(updateParam.getImage());
        }
        siteMapper.updateSite(site);
    }

    public Site getSiteById(Integer id){
        System.out.println("site service");
        return siteMapper.getSite(id);
    }

    public List<Site> getSites(PageParam pageParam){
        return siteMapper.listSites(pageParam);
    }

    public List<Site> getSitesByStadium(Integer stadiumId, PageParam pageParam){
        return siteMapper.listSitesByStadium(stadiumId,pageParam);
    }

    public int getSiteCount(){
        return siteMapper.getSiteCount();
    }
    public int getSiteCountByStadium(Integer stadiumId){
        return siteMapper.getSiteCountByStadium(stadiumId);
    }
}
