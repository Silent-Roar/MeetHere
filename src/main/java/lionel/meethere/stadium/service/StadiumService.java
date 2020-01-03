package lionel.meethere.stadium.service;

import lionel.meethere.paging.PageParam;
import lionel.meethere.site.dao.SiteMapper;
import lionel.meethere.stadium.dao.StadiumMapper;
import lionel.meethere.stadium.entity.Stadium;
import lionel.meethere.stadium.vo.StadiumVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StadiumService {

    @Autowired
    private StadiumMapper stadiumMapper;
    @Autowired
    private SiteMapper siteMapper;

    @SuppressWarnings("AlibabaRemoveCommentedCode")
    public List<StadiumVO> getStadiums (PageParam pageParam){
        return convertToStadiumVOList(stadiumMapper.getStadiumList(pageParam));
    }
    public StadiumVO getStadiumById (Integer id){
        return convertToStadiumVO(stadiumMapper.getStadium(id));
    }

    private StadiumVO convertToStadiumVO(Stadium stadium){
        StadiumVO stadiumVO = new StadiumVO();
        BeanUtils.copyProperties(stadium,stadiumVO);
        stadiumVO.setSiteCount(siteMapper.getSiteCountByStadium(stadium.getId()));
        return stadiumVO;
    }

    private List<StadiumVO> convertToStadiumVOList(List<Stadium> stadiums){
        List<StadiumVO> stadiumVOList = new ArrayList<>();
        for(Stadium stadium : stadiums){
            stadiumVOList.add(convertToStadiumVO(stadium));
        }
        return stadiumVOList;
    }

    public int getStadiumCount(){
        return stadiumMapper.getStadiumCount();
    }

    public int createStadium(Stadium stadium){
        return stadiumMapper.createStadium(stadium);
    }

    public int delteStadium(Integer id){
        return stadiumMapper.deleteStadium(id);
    }

    public int updateStadium(Stadium stadium){
        return stadiumMapper.updateStadium(stadium);
    }

}
